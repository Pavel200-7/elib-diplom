import { ref } from 'vue'
import * as api from '../api/users'

export function useUser() {
    const user = ref(null)
    const users = ref([])

    const page = ref(0)
    const size = ref(20)
    const total = ref(0)

    const filters = {
        query: ''
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
        const responseData = await handleRequest(() => api.getUserPage(criteria))
        users.value = responseData.content
        total.value = responseData.totalElements
        console.log(users.value)
        return responseData
    }

    const getUser = async (id) => {
        const responseData = await handleRequest(() => api.getUserById(id))
        user.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const updateUser = async (id, data) => {
        const responseData = await handleRequest(() => api.updateUser(id, data))
        user.value = responseData
        addNewOrUpdate(id, responseData)
        getUsers()
        return responseData
    }

    const activateUser = async (id) => {
        const responseData = await handleRequest(() => api.activateUser(id))
        user.value = responseData
        addNewOrUpdate(id, responseData)
        getUsers()
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

    const buildCriteria = () => {
        const criteria = {
            searchCriteria: {
                query: filters.query || null
            },
            pageData: {
                page: page.value,
                size: size.value
            }
        }
        return criteria
    }

    const setFilters = (filter) => {
        filters.query = filter.query || ''
        page.value = 0
    }

    const resetFilters = () => {
        filters.query = ''
        page.value = 0
    }

    const statuses = [
        { value: 'CREATED', label: 'Создан' },
        { value: 'ACTIVATED', label: 'Активирован' }
    ]

    return {
        // state
        user,
        users,
        page,
        size,
        total,
        loading,
        error,
        statuses,

        //methods
        getUsers,
        getUser,
        updateUser,
        activateUser,

        setFilters,
        resetFilters
    }
}