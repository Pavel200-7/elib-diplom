import client from '../client/client'

export const getBooking = (id) => {
    return client.get(`/api/v1/bookings/${id}`)
}

export const getUserBooking = (criteria) => {
    return client.get('/api/v1/bookings/user', criteria)
}

export const getUserBookingsPage = (criteria) => {
    return client.get('/api/v1/bookings/user/page', criteria)
}

export const getActiveUserBookings = (userId) => {
    return client.get(`/api/v1/bookings/user/${userId}/active`)
}