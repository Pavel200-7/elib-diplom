import { ref } from 'vue'
import * as api from '../api/literature-groups'

export function useLiteratureGroup() {
    const literatureGroup = ref(null)
    const literatureGroups = ref([])

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

    const getLiteratureGroups = async () => {
        const responseData = await handleRequest(() => api.getAll())
        literatureGroups.value = responseData
        return responseData
    }

    const getLiteratureGroup = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        literatureGroup.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createLiteratureGroup = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        literatureGroup.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateLiteratureGroup = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        literatureGroup.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = literatureGroups.value.findIndex((literatureGroup) => literatureGroup.id === id)
        if (index !== -1) {
            const newLanguages = [...literatureGroups.value]
            newLanguages[index] = data
            literatureGroups.value = newLanguages
        } else {
            literatureGroups.value = [...literatureGroups.value, data]
        }
    }

    const deleteLiteratureGroup = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        literatureGroups.value = literatureGroups.value.filter((literatureGroup) => literatureGroup.id !== id)
        if (literatureGroup.value?.id === id) {
            literatureGroup.value = null
        }
    }

    return {
        // state
        literatureGroup,
        literatureGroups,
        loading,
        error,

        //methods
        getLiteratureGroups,
        getLiteratureGroup,
        createLiteratureGroup,
        updateLiteratureGroup,
        deleteLiteratureGroup
    }
}
