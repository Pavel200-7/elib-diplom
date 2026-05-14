import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(localStorage.getItem('access_token') || null)
    const refreshToken = ref(localStorage.getItem('refresh_token') || null)
    const user = ref(null)
    const roles = ref([])

    const isAuthenticated = computed(() => !!accessToken.value)

    function setTokens(access, refresh) {
        console.log('setTokens called, access:', access ? 'present' : 'missing')  // ← добавить лог

        accessToken.value = access
        refreshToken.value = refresh
        localStorage.setItem('access_token', access)
        localStorage.setItem('refresh_token', refresh)
    }

    function clearTokens() {
        accessToken.value = null
        refreshToken.value = null
        user.value = null
        roles.value = []
        localStorage.removeItem('access_token')
        localStorage.removeItem('refresh_token')
    }

    function setUser(userData) {
        user.value = userData
    }

    function setRoles(userRoles) {
        roles.value = userRoles
    }

    function logout() {
        clearTokens()
        window.location.href = 'http://localhost:8080/api/v1/auth/authorize'
    }

    // Проверка роли
    function hasRole(role) {
        return roles.value.includes(role)
    }

    function isAdmin() {
        return hasRole('ADMIN')
    }

    function isManager() {
        return hasRole('MANAGER')
    }

    function isLibrarian() {
        return hasRole('LIBRARIAN')
    }

    return {
        accessToken,
        refreshToken,
        user,
        roles,
        isAuthenticated,
        setTokens,
        clearTokens,
        setUser,
        setRoles,
        logout,
        hasRole,
        isAdmin,
        isManager,
        isLibrarian
    }
})