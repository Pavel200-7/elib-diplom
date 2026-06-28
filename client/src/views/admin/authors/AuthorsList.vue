<template>    
    <DictionaryTable
        :title="'Авторы'"
        :displayFields="displayFields"
        :rules="rules"
        :items="authors"
        :loading="loading"
        @load="getAuthors"
        @create="(data) => createAuthor(data)"
        @update="(id, data) => updateAuthor(id, data)"
        @delete="(id) => deleteAuthor(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/views/admin/dictionaries/components/DictionaryTable.vue'
import { useAuthor } from '@/services/composables/useAuthor'
import { useCountry } from '@/services/composables/useCountry'
import { onMounted, computed } from 'vue'

const {
  authors,
  loading,
  getAuthors,
  createAuthor,
  updateAuthor,
  deleteAuthor
} = useAuthor()

const {
  countries,
  getCountries
} = useCountry()


const rules = {
    name: [{ required: true, message: 'Введите ФИО автора', trigger: 'blur' }],
    countryId: [{ required: true, message: 'Выберите страну', trigger: 'change' }]
}

const displayFields = computed(() => [
        { 
            key: 'name',
            value: 'name',         
            label: 'ФИО',    
            type: 'text',          
            maxLength: 100
        },
        { 
            key: 'countryId',
            value: 'countryName',        
            label: 'Страна',    
            type: 'select',          
            options: countriesList.value
        }
]) 

const countriesList = computed(() => {
    return countries.value.map(item => ({
        value: item.id,
        label: item.name
    }))
})

onMounted(async () => {
    await getCountries()
})
</script>