import { ref } from 'vue'
import * as api from '../api/circulations'

export function useCirculation() {

    const booking = ref(null)

    const loading = ref(false)
    const error = ref(null)

    const handleRequest = async (request) => {
        loading.value = true
        error.value = null
        try {
            const response = await request()
            return response.data;
        } catch (err) {
            error.value = err.response?.data?.message || err.message
            throw err
        } finally {
            loading.value = false
        }
    }

    const issueCopy = async (userId, copyId) => {
        const responseData = await handleRequest(() => api.issueCopy(userId, copyId))
        booking.value = responseData
        return responseData
    }

    const returnCopy = async (bookingId) => {
        const responseData = await handleRequest(() => api.returnCopy(bookingId))
        booking.value = responseData
        return responseData
    }

    return {
        // state
        booking,
        loading,
        error,

        // methods
        issueCopy,
        returnCopy
    }
}