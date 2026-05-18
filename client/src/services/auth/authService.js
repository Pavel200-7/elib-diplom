import { tokenManager } from './tokenManager'
import apiClient from '@/services/api/client'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL
const AUTH_URL = `${API_BASE_URL}/api/v1/auth/authorize`

class AuthService {
    getAuthUrl() {
        return AUTH_URL
    }

    isAuthenticated() {
        return tokenManager.isAuthenticated()
    }

    isTokenExpired() {
        return tokenManager.isTokenExpired()
    }

    getCurrentUser() {
        const payload = tokenManager.getTokenPayload()
        if (!payload) return null
        
        // Нормализуем роли: приводим к верхнему регистру для единообразия
        let roles = payload.spring_sec_roles || payload.realm_access?.roles || []
        if (Array.isArray(roles)) {
            roles = roles.map(r => r.toUpperCase())
        }
        
        return {
            id: payload.sub,
            email: payload.email,
            username: payload.preferred_username,
            roles: roles
        }
    }

    hasRole(role) {
        const user = this.getCurrentUser()
        if (!user) return false
        // Сравниваем в верхнем регистре
        const roleUpper = role.toUpperCase()
        return user.roles.includes(roleUpper)
    }

    isAdmin() {
        return this.hasRole('ADMIN')
    }

    async refreshToken() {
        const currentRefreshToken = tokenManager.getRefreshToken()
        if (!currentRefreshToken) {
            this.logout()
            return false
        }

        try {
            const response = await apiClient.post('/api/v1/auth/refresh', { 
                refreshToken: currentRefreshToken 
            })
            const { accessToken, refreshToken: newRefreshToken } = response.data
            
            tokenManager.setTokens(accessToken, newRefreshToken || currentRefreshToken)
            return true
        } catch (error) {
            console.error('Token refresh failed:', error)
            this.logout()
            return false
        }
    }

    logout() {
        tokenManager.clearTokens()
        window.location.href = this.getAuthUrl()
    }
}

export const authService = new AuthService()