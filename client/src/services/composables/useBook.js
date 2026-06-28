import { ref } from 'vue'
import * as api from '../api/books'

export function useBook() {
    const book = ref(null)
    const books = ref([])

    const page = ref(0)
    const size = ref(20)
    const total = ref(0)

    const filters = {
        name: '',
        authorId: null,
        genreId: null
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

    const getBooks = async () => {
        const criteria = buildCriteria()
        const responseData = await handleRequest(() => api.getAll(criteria))
        books.value = responseData.content
        total.value = responseData.totalElements
        return responseData
    }

    // const getNextPage = async () => {
    //     const lastShown = (page + 1) * size
    //     if (lastShown < total) {
    //         page.value++
    //     }
    //     return getBooks()
    // }

    // const getGetPrevious = async () => {
    //     if (page.value !== 0) {
    //         page.value--
    //     }
    //     return getBooks()
    // }

    const getBook = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createBook = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        book.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateBook = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        book.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = books.value.findIndex((book) => book.id === id)
        if (index !== -1) {
            const newItems = [...books.value]
            newItems[index] = data
            books.value = newItems
        } else {
            books.value = [...books.value, data]
        }
    }

    const deleteBook = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        books.value = books.value.filter((book) => book.id !== id)
        if (book.value?.id === id) {
            book.value = null
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
        book,
        books,
        page,
        size,
        total,
        loading,
        error,

        //methods
        getBooks,
        getBook,
        createBook,
        updateBook,
        deleteBook,

        setFilters,
        resetFilters
    }
}
