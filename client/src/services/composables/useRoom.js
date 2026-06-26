import { ref } from 'vue'
import * as api from '../api/rooms'

export function useRoom() {
    const room = ref(null)
    const rooms = ref([])

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

    const getRooms = async () => {
        const responseData = await handleRequest(() => api.getAll())
        rooms.value = responseData
        return responseData
    }

    const getRoom = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const createRoom = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        room.value = responseData
        addNewOrUpdate(responseData.id, responseData)
        return responseData
    }

    const updateRoom = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        room.value = responseData
        addNewOrUpdate(id, responseData)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = rooms.value.findIndex((room) => room.id === id)
        if (index !== -1) {
            const newLanguages = [...rooms.value]
            newLanguages[index] = data
            rooms.value = newLanguages
        } else {
            rooms.value = [...rooms.value, data]
        }
    }

    const deleteRoom = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        rooms.value = rooms.value.filter((room) => room.id !== id)
        if (room.value?.id === id) {
            room.value = null
        }
    }


    return {
        // state
        room,
        rooms,
        loading,
        error,

        //methods
        getRooms,
        getRoom,
        createRoom,
        updateRoom,
        deleteRoom
    }
}
