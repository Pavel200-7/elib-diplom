import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { tokenManager } from '@/services/auth/tokenManager'
import { authService } from '@/services/auth/authService'

const AUTH_URL = import.meta.env.VITE_AUTH_URL

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(tokenManager.getAccessToken())
    const refreshTokenValue = ref(tokenManager.getRefreshToken())  // ← переименовали
    const user = ref(authService.getCurrentUser())
    const roles = ref(user.value?.roles || [])

    const isAuthenticated = computed(() => tokenManager.isAuthenticated())
    const isTokenExpired = computed(() => tokenManager.isTokenExpired())

    function setTokens(access, refresh) {
        tokenManager.setTokens(access, refresh)
        accessToken.value = access
        refreshTokenValue.value = refresh  // ← обновляем
        
        const userData = authService.getCurrentUser()
        user.value = userData
        roles.value = userData?.roles || []
    }

    function clearTokens() {
        tokenManager.clearTokens()
        accessToken.value = null
        refreshTokenValue.value = null
        user.value = null
        roles.value = []
    }

    async function refreshToken() {
        const success = await authService.refreshToken()
        if (success) {
            accessToken.value = tokenManager.getAccessToken()
            refreshTokenValue.value = tokenManager.getRefreshToken()
            const userData = authService.getCurrentUser()
            user.value = userData
            roles.value = userData?.roles || []
        }
        return success
    }

    function logout() {
        clearTokens()
    }

    function hasRole(role) {
        return authService.hasRole(role)
    }

    function isAdmin() {
        return authService.isAdmin()
    }

    function hasAnyRole(requiredRoles) {
        if (!requiredRoles || requiredRoles.length === 0) return true
        return requiredRoles.some(role => roles.value.includes(role) || roles.value.includes(`ROLE_${role}`))
    }

    function scheduleTokenRefresh() {
        const expirationTime = tokenManager.getTokenExpirationTime()
        if (!expirationTime) return
        
        const now = Date.now()
        const timeUntilExpiry = expirationTime - now
        const refreshTime = timeUntilExpiry - 5 * 60 * 1000
        
        if (refreshTime > 0) {
            setTimeout(async () => {
                if (tokenManager.isAuthenticated()) {
                    await refreshToken()
                    scheduleTokenRefresh()
                }
            }, refreshTime)
        }
    }

    return {
        accessToken,
        refreshToken: refreshTokenValue,  // ← экспортируем под старым именем
        user,
        roles,
        isAuthenticated,
        isTokenExpired,
        setTokens,
        clearTokens,
        refreshToken,
        logout,
        hasRole,
        isAdmin,
        hasAnyRole,
        scheduleTokenRefresh
    }
})