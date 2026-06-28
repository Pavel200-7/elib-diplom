import { ref } from 'vue'
import * as api from '../api/holders'

export function useHolder() {
    const holder = ref(null)
    const holders = ref([])

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

    const getHolders = async () => {
        const responseData = await handleRequest(() => api.getAll())
        holders.value = responseData.map(item => buildModel(item))
        console.log(holders.value)
        return responseData
    }

    const getHolder = async (id) => {
        const responseData = await handleRequest(() => api.getById(id))
        holder.value = buildModel(responseData)
        return responseData
    }

    const createHolder = async (data) => {
        const responseData = await handleRequest(() => api.create(data))
        holder.value = buildModel(responseData)
        addNewOrUpdate(responseData.id, holder.value)
        return responseData
    }

    const updateHolder = async (id, data) => {
        const responseData = await handleRequest(() => api.update(id, data))
        holder.value = buildModel(responseData)
        addNewOrUpdate(id, holder.value)
        return responseData
    }

    const addNewOrUpdate = (id, data) => {
        const index = holders.value.findIndex((holder) => holder.id === id)
        if (index !== -1) {
            const newItems = [...holders.value]
            newItems[index] = data
            holders.value = newItems
        } else {
            holders.value = [...holders.value, data]
        }
    }

    const deleteHolder = async (id) => {
        await handleRequest(() => api.deleteItem(id))
        holders.value = holders.value.filter((holder) => holder.id !== id)
        if (holder.value?.id === id) {
            holder.value = null
        }
    }

    const buildModel = (data) => {
        let model = data
        model = addTypeLabel(data)
        return model
    }

    const addTypeLabel = (data) => { 
        return {
            ...data,
            typeLabel: getHolderTypeLabel(data.type)
        }
    }

    const getHolderTypeLabel = (type) => 
        holderTypes.find(item => item.value == type).label || type

    const holderTypes = [
        { value: 'SHELF', label: 'Полка' },
        { value: 'CABINET', label: 'Шкаф' },
        { value: 'DEPOSITORY', label: 'Книгохранилище' },
        { value: 'RACK', label: 'Стеллаж' },
        { value: 'DISPLAY', label: 'Витрина' },
        { value: 'LOCKER', label: 'Сейф' }
    ];

    return {
        // state
        holder,
        holders,
        loading,
        error,
        holderTypes,

        // methods
        getHolders,
        getHolder,
        createHolder,
        updateHolder,
        deleteHolder,
    }
}