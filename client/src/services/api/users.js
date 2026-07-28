import client from '../client/client'

export const getUserPage = (criteria) => {
    return client.post(`/api/v1/users/page`, criteria)
}

export const getUserById = (id) => {
    return client.get(`/api/v1/users/${id}`)
}

export const updateUser = (id, data) => {
    return client.put(`/api/v1/users/${id}`, data)
}

export const activateUser = (id) => {
    return client.post(`/api/v1/users/${id}/activate`)
}