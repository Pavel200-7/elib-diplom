import { ref } from 'vue'
import * as api from '../api/countries'

export function useCountry() {
    const country = ref(null)
    const countries = ref([])

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

    const getCountries = async () => {
        const responseData = await handleRequest(() => api.getAll())
        countries.value = responseData
        return responseData
    }

    const getCountry = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createCountry = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        country.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateCountry = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        country.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = countries.value.findIndex((country) => country.id === id)
        if (index !== -1) {
            const newItems = [...countries.value]
            newItems[index] = data
            countries.value = newItems
        } else {
            countries.value = [...countries.value, data]
        }
    }

    const deleteCountry = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        countries.value = countries.value.filter((country) => country.id !== id)
        if (country.value?.id === id) {
            country.value = null
        }
    }

    return {
        // state
        country,
        countries,
        loading,
        error,

        //methods
        getCountries,
        getCountry,
        createCountry,
        updateCountry,
        deleteCountry
    }
}
