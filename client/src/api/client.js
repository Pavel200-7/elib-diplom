import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Request interceptor — добавляем токен
apiClient.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore()
        if (authStore.accessToken) {
            config.headers.Authorization = `Bearer ${authStore.accessToken}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

// Response interceptor — обрабатываем 401
apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config
        
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true
            
            const authStore = useAuthStore()
            const refreshToken = authStore.refreshToken
            
            if (refreshToken) {
                try {
                    const response = await axios.post(
                        `${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/refresh`,
                        { refreshToken }
                    )
                    
                    const { accessToken: newAccessToken, refreshToken: newRefreshToken } = response.data
                    authStore.setTokens(newAccessToken, newRefreshToken)
                    
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
                    return apiClient(originalRequest)
                } catch (refreshError) {
                    authStore.logout()
                    return Promise.reject(refreshError)
                }
            } else {
                authStore.logout()
            }
        }
        
        return Promise.reject(error)
    }
)

export default apiClient