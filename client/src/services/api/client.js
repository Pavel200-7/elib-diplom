import axios from 'axios'
import { tokenManager } from '@/services/auth/tokenManager'
import { authService } from '@/services/auth/authService'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Флаг для предотвращения множественных запросов на обновление токена
let isRefreshing = false
let failedQueue = []

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error)
        } else {
            prom.resolve(token)
        }
    })
    failedQueue = []
}

// Request interceptor — добавляем токен
apiClient.interceptors.request.use(
    (config) => {
        const token = tokenManager.getAccessToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
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
        
        // Если не 401 или запрос уже повторялся — отклоняем
        if (error.response?.status !== 401 || originalRequest._retry) {
            return Promise.reject(error)
        }
        
        // Если уже идёт процесс обновления — ставим в очередь
        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject })
            }).then(token => {
                originalRequest.headers.Authorization = `Bearer ${token}`
                return apiClient(originalRequest)
            }).catch(err => Promise.reject(err))
        }
        
        originalRequest._retry = true
        isRefreshing = true
        
        try {
            const refreshToken = tokenManager.getRefreshToken()
            
            if (!refreshToken) {
                throw new Error('No refresh token')
            }
            
            const response = await axios.post(
                `${API_BASE_URL}/api/v1/auth/refresh`,
                { refreshToken }
            )
            
            const { accessToken, refreshToken: newRefreshToken } = response.data
            tokenManager.setTokens(accessToken, newRefreshToken || refreshToken)
            

            // Обрабатываем очередь ожидающих запросов
            processQueue(null, accessToken)
            
            // Повторяем оригинальный запрос с новым токеном
            originalRequest.headers.Authorization = `Bearer ${accessToken}`
            return apiClient(originalRequest)
            
        } catch (refreshError) {
            processQueue(refreshError, null)
            authService.logout()
            return Promise.reject(refreshError)
        } finally {
            isRefreshing = false
        }
    }
)

export default apiClient