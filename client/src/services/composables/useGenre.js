import { ref } from 'vue'
import * as api from '../api/genres'

export function useGenre() {

    const genre = ref(null)
    const genres = ref([])

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

    const getGenres = async () => {
        const responseData = await handleRequest(() => api.getAll())
        genres.value = responseData
        return responseData
    }

    const getGenre = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        genre.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createGenre = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        genre.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateGenre = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        genre.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = genres.value.findIndex((genre) => genre.id === id)
        if (index !== -1) {
            const newItems = [...genres.value]
            newItems[index] = data
            genres.value = newItems
        } else {
            genres.value = [...genres.value, data]    
        }
    }

    const deleteGenre = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        genres.value = genres.value.filter((genre) => genre.id !== id)
        if (genre.value?.id === id) {
            genre.value = null
        }
    }

    return {
        // state
        genre,
        genres,
        loading,
        error,

        //methods
        getGenres,
        getGenre,
        createGenre,
        updateGenre,
        deleteGenre
    }
}