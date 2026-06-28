import { ref } from 'vue'
import * as api from '../api/publishings'

export function usePublishing() {
    const publishing = ref(null)
    const publishings = ref([])

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

    const getPublishings = async () => {
        const responseData = await handleRequest(() => api.getAll())
        publishings.value = responseData
        return responseData
    }

    const getPublishing = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        publishing.value = responseData
        return responseData
    }

    const createPublishing = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        publishing.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updatePublishing = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        publishing.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = publishings.value.findIndex((publishing) => publishing.id === id)
        if (index !== -1) {
            const newItems = [...publishings.value]
            newItems[index] = data
            publishings.value = newItems
        } else {
            publishings.value = [...publishings.value, data]
        }
    }

    const deletePublishing = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        publishings.value = publishings.value.filter((publishing) => publishing.id !== id)
        if (publishing.value?.id === id) {
            publishing.value = null
        }
    }


    return {
        // state
        publishing,
        publishings,
        loading,
        error,

        // methods
        getPublishings,
        getPublishing,
        createPublishing,
        updatePublishing,
        deletePublishing,
    }
}