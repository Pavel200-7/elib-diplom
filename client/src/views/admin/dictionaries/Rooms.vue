<template>
    <DictionaryTable
        :title="'Комнаты'"
        :displayFields="displayFields"
        :rules="rules"
        :items="rooms"
        :loading="loading"
        @load="getRooms"
        @create="(data) => createRoom(data)"
        @update="(id, data) =>  updateRoom(id, data)"
        @delete="(id) => deleteRoom(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/components/dictionaries/DictionaryTable.vue'
import { useRoom } from '@/services/composables/useRoom'

const {
  rooms,
  loading,
  getRooms,
  createRoom,
  updateRoom,
  deleteRoom
} = useRoom()

const displayFields = [
    { 
        key: 'name',
        value: 'name',          
        label: 'Название',    
        type: 'text',          
        maxLength: 100
    }
]

const rules = {
    name: [                   
        { required: true, message: 'Введите название', trigger: 'blur' }
    ]
}
</script>