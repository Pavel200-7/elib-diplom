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
            const responseData = await request()
            return responseData.data;
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
        country.value = responseData
        return responseData
    }

    const createCountry = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        country.value = responseData
        countries.value = [...countries.value, responseData]
        return responseData
    }

    const updateCountry = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        country.value = responseData
        
        const index = countries.value.findIndex((country) => country.id === responseData.id)
        if (index !== -1) {
            const newCountries = [...countries.value]
            newCountries[index] = responseData
            countries.value = newCountries
        } else {
            countries.value = [...countries.value, responseData]
        }

        return responseData
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
