import client from './client'

export const getAll = () => {
    return client.get(`/api/v1/countries`)
}

export const getById = (id) => {
    return client.get(`/api/v1/countries/${id}`)
}

export const create = (data) => {
    return client.post(`/api/v1/countries`, data)
}

export const update = (id, data) => {
    return client.put(`/api/v1/countries/${id}`, data)
}

export const deleteItem = (id) => {
    return client.delete(`/api/v1/countries/${id}`)
}