<template>
    <DictionaryTable
        :title="'Места хранения'"
        :displayFields="displayFields"
        :rules="rules"
        :items="holders"
        :loading="loading"
        @load="getHolders"
        @create="(data) => createHolder(data)"
        @update="(id, data) => updateHolder(id, data)"
        @delete="(id) => deleteHolder(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/views/admin/dictionaries/components/DictionaryTable.vue'
import { useHolder } from '@/services/composables/useHolder'
import { useRoom } from '@/services/composables/useRoom'
import { onMounted, computed } from 'vue'

const {
  holders,
  loading,
  holderTypes,
  getHolders,
  createHolder,
  updateHolder,
  deleteHolder
} = useHolder()

const {
  rooms,
  getRooms,
} = useRoom()

const displayFields = computed(() => [
        { 
            key: 'name',
            value: 'name',         
            label: 'Название',    
            type: 'text',          
            maxLength: 100
        },
        { 
            key: 'roomId',
            value: 'roomName',         
            label: 'Комната',    
            type: 'select',          
            options: roomsList.value
        },
        { 
            key: 'type',
            value: 'typeLabel',        
            label: 'Тип',    
            type: 'select',          
            options: holderTypes
        }
]) 

const roomsList = computed(() => {
    return rooms.value.map(item => ({
        value: item.id,
        label: item.name
    }))
})

const rules = {
    name: [{ required: true, message: 'Введите название', trigger: 'blur' }],
    roomId: [{ required: true, message: 'Выберите помещение', trigger: 'change' }],
    type: [{ required: true, message: 'Выберите тип', trigger: 'change' }]
}

onMounted(async () => {
    await getRooms()
})
</script>