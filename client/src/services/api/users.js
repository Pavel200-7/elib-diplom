import client from './client'

export const searchUsers = (query) => {
    return client.get('/api/v1/users/search', { params: { query } })
}

export const getUserById = (id) => {
    return client.get(`/api/v1/users/${id}`)
}

export const getAllUsers = (params) => {
    return client.get('/api/v1/users', { params })
}

export const createUser = (data) => {
    return client.post('/api/v1/users', data)
}

export const updateUser = (id, data) => {
    return client.put(`/api/v1/users/${id}`, data)
}

export const deleteUser = (id) => {
    return client.delete(`/api/v1/users/${id}`)
}

export const activateUser = (id) => {
    return client.post(`/api/v1/users/${id}/activate`)
}