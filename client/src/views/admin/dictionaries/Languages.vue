<template>
    <DictionaryTable
        :title="'Языки'"
        :displayFields="displayFields"
        :rules="rules"
        :items="languages"
        :loading="loading"
        @load="getLanguages"
        @create="(data) => createLanguage(data)"
        @update="(id, data) => updateLanguage(id, data)"
        @delete="(id) => deleteLanguage(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/views/admin/dictionaries/components/DictionaryTable.vue'
import { useLanguage } from '@/services/composables/useLanguage'

const {
  languages,
  loading,
  getLanguages,
  createLanguage,
  updateLanguage,
  deleteLanguage
} = useLanguage()

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