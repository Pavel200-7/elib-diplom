<template>
    <DictionaryTable
        :title="'Жанры'"
        :displayFields="displayFields"
        :rules="rules"
        :items="genres"
        :loading="loading"
        @load="getGenres"
        @create="(data) => createGenre(data)"
        @update="(id, data) => updateGenre(id, data)"
        @delete="(id) => deleteGenre(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/components/dictionaries/DictionaryTable.vue'
import { useGenre } from '@/services/composables/useGenre'

const {
  genres,
  loading,
  getGenres,
  createGenre,
  updateGenre,
  deleteGenre
} = useGenre()

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