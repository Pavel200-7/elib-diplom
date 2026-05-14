import client from './client'

export const refreshToken = (refreshToken) => {
    return client.post('/api/v1/auth/refresh', { refreshToken })
}

export const logout = (refreshToken) => {
    return client.post('/api/v1/auth/logout', { refreshToken })
}