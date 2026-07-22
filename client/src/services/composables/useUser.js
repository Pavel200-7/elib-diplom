import { ref } from 'vue'
import * as api from '../api/users'

export function useBook() {
    const user = ref(null)
    const users = ref([])

    const page = ref(0)
    const size = ref(20)
    const total = ref(0)

    const filters = {
        search: '',
    }

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

    const getUsers = async () => {
        const criteria = buildCriteria()
        const responseData = await handleRequest(() => api.getAll(criteria))
        users.value = responseData.content
        total.value = responseData.totalElements
        return responseData
    }

    const getBook = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        user.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createBook = async (data) => {
        console.log(data)
        const responseData = await handleRequest(() => api.create(data))
        user.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateBook = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        user.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = users.value.findIndex((user) => user.id === id)
        if (index !== -1) {
            const newItems = [...users.value]
            newItems[index] = data
            users.value = newItems
        } else {
            users.value = [...users.value, data]
        }
    }

    const deleteBook = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        users.value = users.value.filter((user) => user.id !== id)
        if (user.value?.id === id) {
            user.value = null
        }
    }

    const buildCriteria = () => {
        const criteria = {
            searchCriteria: {
                name: filters.name || null,
                authorId: filters.authorId || null,
                genreId: filters.genreId || null
            },
            sortCriteria: {
                sortBy: 'NAME',
                sortDirection: 'ASC'
            },
            pageData: {
                page: page.value,
                size: size.value
            }
        }
        return criteria
    }

    const setFilters = (filter) => {
        filters.name = filter.name
        filters.authorId = filter.authorId
        filters.genreId = filter.genreId
        page.value = 0
    }

    const resetFilters = () => {
        filters.name = ''
        filters.authorId = null
        filters.genreId = null
        page.value = 0
    }

    return {
        // state
        user,
        users,
        page,
        size,
        total,
        loading,
        error,

        //methods
        getUsers,
        getBook,
        createBook,
        updateBook,
        deleteBook,

        setFilters,
        resetFilters
    }
}