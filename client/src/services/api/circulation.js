import client from '../client/client'

// ==================== БРОНИРОВАНИЕ ====================

export const reserveBook = (userId, bookId) => {
    return client.post('/api/v1/circulation/reserve/book', null, {
        params: { userId, bookId }
    })
}

export const reserveCopy = (userId, copyId) => {
    return client.post('/api/v1/circulation/reserve/copy', null, {
        params: { userId, copyId }
    })
}

export const cancelReservation = (bookingId) => {
    return client.put(`/api/v1/circulation/reservation/${bookingId}`)
}

// ==================== ВЫДАЧА ====================

export const issueFromReservation = (bookingId) => {
    return client.put(`/api/v1/circulation/issue/from-reservation/${bookingId}`)
}

export const issueDirectBook = (userId, bookId) => {
    return client.post('/api/v1/circulation/issue/direct/book', null, {
        params: { userId, bookId }
    })
}

export const issueDirectCopy = (userId, copyId) => {
    return client.post('/api/v1/circulation/issue/direct/copy', null, {
        params: { userId, copyId }
    })
}

// ==================== ВОЗВРАТ ====================

export const returnBook = (bookingId) => {
    return client.put(`/api/v1/circulation/return/${bookingId}`)
}

// ==================== ПОЛУЧЕНИЕ СПИСКОВ ДЛЯ ЦИРКУЛЯЦИИ ====================

// Доступные копии (используем /page с фильтром по статусу AVAILABLE)
export const getAvailableCopies = async (params) => {
    const criteria = {
        searchCriteria: {
            status: 'AVAILABLE',
            inventoryNumber: params?.search || null,
            isbn: null,
            holderId: null,
            bookId: null
        },
        sortCriteria: {
            sortBy: 'INVENTORY_NUMBER',
            sortDirection: 'ASC'
        },
        pageData: {
            page: 0,
            size: 100
        }
    }
    const response = await client.post('/api/v1/copies/page', criteria)
    return { data: response.data.content }
}

// Зарезервированные (нужны Booking с copy)
export const getReservations = async (params) => {
    const criteria = {
        status: 'RESERVED',
        ...params
    }
    const response = await client.post('/api/v1/bookings/user/page', criteria)
    return { data: response.data }
}

// Выданные (нужны Booking с copy)
export const getIssued = async (params) => {
    const criteria = {
        status: 'ISSUED',
        ...params
    }
    const response = await client.post('/api/v1/bookings/user/page', criteria)
    return { data: response.data }
}

// ==================== ПОЛУЧЕНИЕ БРОНЕЙ ПОЛЬЗОВАТЕЛЯ ====================

export const getUserBookingsPage = (criteria) => {
    return client.post('/api/v1/bookings/user/page', criteria)
}

export const getUserReservations = (userId) => {
    return getUserBookingsPage({
        userId: userId,
        status: 'RESERVED'
    })
}

export const getUserIssued = (userId) => {
    return getUserBookingsPage({
        userId: userId,
        status: 'ISSUED'
    })
}

export const getUserHistory = (userId) => {
    return getUserBookingsPage({
        userId: userId,
        status: 'CLOSED'
    })
}

export const getUserOverdue = (userId) => {
    return getUserBookingsPage({
        userId: userId,
        overdueOnly: true
    })
}

// ==================== ПОИСК ПОЛЬЗОВАТЕЛЕЙ ====================

export const searchUsers = (query) => {
    return client.get('/api/v1/users/search', { params: { query } })
}