import client from '../client/client'

export const getAll = () => {
    return client.get(`/api/v1/rooms`)
}

export const getById = (id) => {
    return client.get(`/api/v1/rooms/${id}`)
}

export const create = (data) => {
    return client.post(`/api/v1/rooms`, data)
}

export const update = (id, data) => {
    return client.put(`/api/v1/rooms/${id}`, data)
}

export const deleteItem = (id) => {
    return client.delete(`/api/v1/rooms/${id}`)
}