import client from '../client/client'

// Получение страницы экземпляров (POST с критериями)
export const getCopiesPage = (criteria) => {
    return client.post('/api/v1/copies/page', criteria)
}

// Получение одного экземпляра
export const getById = (id) => {
    return client.get(`/api/v1/copies/${id}`)
}

// Создание одного экземпляра
export const create = (data) => {
    return client.post('/api/v1/copies', data)
}

// Массовое создание
export const batchCreate = (data) => {
    return client.post('/api/v1/copies/batch', data)
}

// Обновление экземпляра
export const update = (id, data) => {
    return client.put(`/api/v1/copies/${id}`, data)
}

// Удаление одного экземпляра
export const deleteItem = (id) => {
    return client.delete(`/api/v1/copies/${id}`)
}

// Массовая установка места хранения
export const bulkSetHolder = (data) => {
    return client.patch('/api/v1/copies/holder', data)
}

// Статусные переходы (только доступные на бэкенде)
export const setAvailable = (id) => {
    return client.patch(`/api/v1/copies/${id}/available`)
}

export const setShelved = (id) => {
    return client.patch(`/api/v1/copies/${id}/shelved`)
}

export const setWrittenOff = (id) => {
    return client.patch(`/api/v1/copies/${id}/written-off`)
}