<template>
    <DictionaryTable
        :title="'Страны'"
        :displayFields="displayFields"
        :rules="rules"
        :items="countries"
        :loading="loading"
        @load="getCountries"
        @create="(data) => createCountry(data)"
        @update="(id, data) => updateCountry(id, data)"
        @delete="(id) => deleteCountry(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/components/dictionaries/DictionaryTable.vue'
import { useCountry } from '@/services/composables/useCountry'

const {
  countries,
  loading,
  getCountries,
  createCountry,
  updateCountry,
  deleteCountry
} = useCountry()

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