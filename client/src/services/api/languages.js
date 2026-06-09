import client from './client'

export const getAll = () => {
    return client.get(`/api/v1/languages`)
}

export const getById = (id) => {
    return client.get(`/api/v1/languages/${id}`)
}

export const create = (data) => {
    return client.post(`/api/v1/languages`, data)
}

export const update = (id, data) => {
    return client.put(`/api/v1/languages/${id}`, data)
}

export const deleteItem = (id) => {
    return client.delete(`/api/v1/languages/${id}`)
}