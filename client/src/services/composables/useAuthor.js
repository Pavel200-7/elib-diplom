import { ref } from 'vue'
import * as api from '../api/authors'

export function useAuthor() {
    const author = ref(null)
    const authors = ref([])

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

    const getAuthors = async () => {
        const responseData = await handleRequest(() => api.getAll())
        authors.value = responseData
        return responseData
    }

    const getAuthor = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createAuthor = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        author.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateAuthor = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        author.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = authors.value.findIndex((author) => author.id === id)
        if (index !== -1) {
            const newItems = [...authors.value]
            newItems[index] = data
            authors.value = newItems
        } else {
            authors.value = [...authors.value, data]
        }
    }

    const deleteAuthor = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        authors.value = authors.value.filter((author) => author.id !== id)
        if (author.value?.id === id) {
            author.value = null
        }
    }

    return {
        // state
        author,
        authors,
        loading,
        error,

        //methods
        getAuthors,
        getAuthor,
        createAuthor,
        updateAuthor,
        deleteAuthor
    }
}
