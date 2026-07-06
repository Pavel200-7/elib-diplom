import { ref } from 'vue'
import * as api from '../api/copies'

export function useCopy() {
    const copy = ref(null)
    const copies = ref([])

    const page = ref(0)
    const size = ref(20)
    const total = ref(0)

    const filters = {
        inventoryNumber: null,
        isbn: null,
        holderId: null,
        bookId: null,
        status: null
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

    const getCopies = async () => {
        const criteria = buildCriteria()
        const responseData = await handleRequest(() => api.getCopiesPage(criteria))
        copies.value = responseData.content
        total.value = responseData.totalElements
        console.log(copies.value)
        return responseData
    }

    const getCopy = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createCopy = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        copy.value = responseData
        getCopies()
        return responseData
    }

    const createCopies = async (data) => {
        const responseData = await handleRequest(() => api.batchCreate(data))
        getCopies()
        return responseData
    }

    const updateCopy = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        copy.value = responseData
        getCopies()
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = copies.value.findIndex((copy) => copy.id === id)
        if (index !== -1) {
            const newItems = [...copies.value]
            newItems[index] = data
            copies.value = newItems
        } else {
            copies.value = [...copies.value, data]
        }
    }

    const deleteCopy = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        copies.value = copies.value.filter((copy) => copy.id !== id)
        if (copy.value?.id === id) {
            copy.value = null
        }
    }

    const bulkDeleteCopies = async (ids) => {
        await Promise.all(
            ids.map(id => deleteCopy(id))
        );
    }

    const setAvailable = async (id) => {
        const responseData = await handleRequest(() => api.setAvailable(id))
        copy.value = responseData
        getCopies()
        return responseData

    }

    const setShelved = async (id) => {
        const responseData = await handleRequest(() => api.setShelved(id))
        copy.value = responseData
        getCopies()
        return responseData
    }

    const setWrittenOff = async (id) => {
        const responseData = await handleRequest(() => api.setWrittenOff(id))
        copy.value = responseData
        getCopies()
        return responseData
    }

    const bulkSetHolder = async (holderId, ids) => {
        const responseData = await handleRequest(() => api.bulkSetHolder({
            holderId: holderId,
            copiesId: ids
        }))
        getCopies()
        return responseData
    }  

    const buildCriteria = () => {
        const criteria = {
            searchCriteria: {
                inventoryNumber: filters.inventoryNumber || null,
                isbn: filters.isbn || null,
                holderId: filters.holderId || null,
                bookId: filters.bookId || null,
                status: filters.status || null
            },
            sortCriteria: {
                sortBy: 'INVENTORY_NUMBER',
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
        filters.inventoryNumber = filter.inventoryNumber || ''
        filters.isbn = filter.isbn || ''
        filters.holderId = filter.holderId || null
        filters.bookId = filter.bookId || null
        filters.status = filter.status || ''
        page.value = 0
    }

    const resetFilters = () => {
        filters.inventoryNumber =  ''
        filters.isbn = ''
        filters.holderId =  null
        filters.bookId = null
        filters.status = ''
        page.value = 0
    }

    const statuses = [
        { value: 'ADDED', label: 'Добавлен' },
        { value: 'AVAILABLE', label: 'Доступен' },
        { value: 'IN_TRANSIT', label: 'В обработке' },
        { value: 'RESERVED', label: 'Зарезервирован' },
        { value: 'ISSUED', label: 'Выдан' },
        { value: 'WRITTEN_OFF', label: 'Списан' }
    ]

    return {
        // state
        copy,
        copies,
        page,
        size,
        total,
        loading,
        error,
        statuses,

        //methods
        getCopies,
        getCopy,
        createCopy,
        createCopies,
        updateCopy,
        deleteCopy,
        bulkDeleteCopies,
        setAvailable,
        setShelved,
        setWrittenOff,
        bulkSetHolder,

        setFilters,
        resetFilters
    }
}
