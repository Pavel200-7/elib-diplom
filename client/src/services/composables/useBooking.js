import { ref } from 'vue'
import * as api from '../api/bookings'

export function useBooking() {
    // Состояния
    const booking = ref(null)
    const bookings = ref([])
    const bookingsPage = ref(null)
    const activeBookings = ref([])
    
    // Пагинация
    const page = ref(0)
    const size = ref(20)
    const total = ref(0)
    
    // Фильтры (соответствуют GetBookingCriteria)
    const filters = {
        status: null,
        userId: null,
        copyId: null,
        createdFrom: null,
        createdTo: null,
        overdueOnly: null
    }
    
    const loading = ref(false)
    const error = ref(null)

    const handleRequest = async (request) => {
        loading.value = true
        error.value = null
        try {
            const response = await request()
            return response.data
        } catch (err) {
            error.value = err.response?.data?.message || err.message
            throw err
        } finally {
            loading.value = false
        }
    }

    const buildCriteria = () => {
        const criteria = {
            status: filters.status || null,
            userId: filters.userId || null,
            copyId: filters.copyId || null,
            createdFrom: filters.createdFrom || null,
            createdTo: filters.createdTo || null,
            overdueOnly: filters.overdueOnly || null
        }
        
        // Удаляем null значения
        Object.keys(criteria).forEach(key => {
            if (criteria[key] === null || criteria[key] === undefined) {
                delete criteria[key]
            }
        })
        
        return criteria
    }

    const getUserBooking = async () => {
        const criteria = buildCriteria()
        const responseData = await handleRequest(() => api.getUserBooking(criteria))
        bookings.value = responseData
        return responseData
    }

    const getUserBookingsPage = async () => {
        const criteria = {
            ...buildCriteria(),
            page: page.value,
            size: size.value
        }
        const responseData = await handleRequest(() => api.getUserBookingsPage(criteria))
        bookingsPage.value = responseData
        total.value = responseData.totalElements || responseData.total || 0
        return responseData
    }

    const getBooking = async (id) => {
        const responseData = await handleRequest(() => api.getBooking(id))
        booking.value = responseData
        return responseData
    }

    const getActiveUserBookings = async (userId) => {
        const responseData = await handleRequest(() => api.getActiveUserBookings(userId))
        activeBookings.value = responseData
        return responseData
    }

    const setFilters = (filter) => {
        filters.status = filter.status ?? filters.status
        filters.userId = filter.userId ?? filters.userId
        filters.copyId = filter.copyId ?? filters.copyId
        filters.createdFrom = filter.createdFrom ?? filters.createdFrom
        filters.createdTo = filter.createdTo ?? filters.createdTo
        filters.overdueOnly = filter.overdueOnly ?? filters.overdueOnly
        page.value = 0
    }

    const resetFilters = () => {
        filters.status = null
        filters.userId = null
        filters.copyId = null
        filters.createdFrom = null
        filters.createdTo = null
        filters.overdueOnly = null
        page.value = 0
    }

    return {
        // state
        booking,
        bookings,
        bookingsPage,
        activeBookings,
        page,
        size,
        total,
        loading,
        error,

        // methods
        getBooking,
        getUserBooking,
        getUserBookingsPage,
        getActiveUserBookings,
        setFilters,
        resetFilters
    }
}