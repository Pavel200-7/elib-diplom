import client from '../client/client'

export const getAll = (entity) => {
    return client.get(`/api/v1${entity}`)
}

export const getById = (entity, id) => {
    return client.get(`/api/v1${entity}/${id}`)
}

export const create = (entity, data) => {
    return client.post(`/api/v1${entity}`, data)
}

export const update = (entity, id, data) => {
    return client.put(`/api/v1${entity}/${id}`, data)
}

export const deleteItem = (entity, id) => {
    return client.delete(`/api/v1${entity}/${id}`)
}