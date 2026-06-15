import client from '../client/client'

export const getAll = (criteria) => {
    return client.post('/api/v1/books/page', criteria)
}

export const getById = (id) => {
    return client.get(`/api/v1/books/${id}`)
}

export const create = (data) => {
    return client.post('/api/v1/books', data)
}

export const update = (id, data) => {
    return client.put(`/api/v1/books/${id}`, data)
}

export const deleteItem = (id) => {
    return client.delete(`/api/v1/books/${id}`)
}

export const getAvailableCount = (id) => {
    return client.get(`/api/v1/books/${id}/available/count`)
}

