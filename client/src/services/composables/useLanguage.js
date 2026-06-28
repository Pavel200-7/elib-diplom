import { ref } from 'vue'
import * as api from '../api/languages'

export function useLanguage() {
    const language = ref(null)
    const languages = ref([])

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

    const getLanguages = async () => {
        const responseData = await handleRequest(() => api.getAll())
        languages.value = responseData
        return responseData
    }

    const getLanguage = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createLanguage = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        language.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateLanguage = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        language.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = languages.value.findIndex((language) => language.id === id)
        if (index !== -1) {
            const newItems = [...languages.value]
            newItems[index] = data
            languages.value = newItems
        } else {
            languages.value = [...languages.value, data]
        }
    }

    const deleteLanguage = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        languages.value = languages.value.filter((language) => language.id !== id)
        if (language.value?.id === id) {
            language.value = null
        }
    }


    return {
        // state
        language,
        languages,
        loading,
        error,

        //methods
        getLanguages,
        getLanguage,
        createLanguage,
        updateLanguage,
        deleteLanguage
    }
}
