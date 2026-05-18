class TokenManager {
    constructor() {
        this.accessTokenKey = 'access_token'
        this.refreshTokenKey = 'refresh_token'
    }

    getAccessToken() {
        return localStorage.getItem(this.accessTokenKey)
    }

    getRefreshToken() {
        return localStorage.getItem(this.refreshTokenKey)
    }

    setTokens(accessToken, refreshToken) {
        if (accessToken) localStorage.setItem(this.accessTokenKey, accessToken)
        if (refreshToken) localStorage.setItem(this.refreshTokenKey, refreshToken)
    }

    clearTokens() {
        localStorage.removeItem(this.accessTokenKey)
        localStorage.removeItem(this.refreshTokenKey)
    }

    isAuthenticated() {
        const token = this.getAccessToken()
        if (!token) return false
        
        try {
            const payload = JSON.parse(atob(token.split('.')[1]))
            const exp = payload.exp * 1000
            return Date.now() < exp
        } catch (e) {
            return false
        }
    }

    isTokenExpired() {
        const token = this.getAccessToken()
        if (!token) return true
        
        try {
            const payload = JSON.parse(atob(token.split('.')[1]))
            const exp = payload.exp * 1000
            return Date.now() >= exp
        } catch (e) {
            return true
        }
    }

    getTokenExpirationTime() {
        const token = this.getAccessToken()
        if (!token) return null
        
        try {
            const payload = JSON.parse(atob(token.split('.')[1]))
            return payload.exp * 1000
        } catch (e) {
            return null
        }
    }

    getTokenPayload() {
        const token = this.getAccessToken()
        if (!token) return null
        try {
            return JSON.parse(atob(token.split('.')[1]))
        } catch (e) {
            return null
        }
    }
}

export const tokenManager = new TokenManager()