<template>
    <DictionaryTable
        :title="'Издательства'"
        :displayFields="displayFields"
        :rules="rules"
        :items="publishings"
        :loading="loading"
        @load="getPublishings"
        @create="(data) => createPublishing(data)"
        @update="(id, data) => updatePublishing(id, data)"
        @delete="(id) => deletePublishing(id)"
    />

</template>

<script setup>
import DictionaryTable from '@/views/admin/dictionaries/components/DictionaryTable.vue'
import { usePublishing } from '@/services/composables/usePublishing'
import { useCountry } from '@/services/composables/useCountry'
import { onMounted, computed } from 'vue'


const {
  publishings,
  loading,
  getPublishings,
  createPublishing,
  updatePublishing,
  deletePublishing
} = usePublishing()

const {
  countries,
  getCountries
} = useCountry()

const countriesList = computed(() => {
    return countries.value.map(item => ({
        value: item.id,
        label: item.name
    }))
})

const displayFields = computed(() => [
        { 
            key: 'name',
            value: 'name',         
            label: 'Название',    
            type: 'text',          
            maxLength: 100
        },
        { 
            key: 'description',
            value: 'description',         
            label: 'Описание',    
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

const rules = {
    name: [{ required: true, message: 'Введите название издательства', trigger: 'blur' }],
    description: [{ required: false, message: 'Введите описание', trigger: 'blur' }],
    countryId: [{ required: true, message: 'Выберите страну', trigger: 'change' }]
}

onMounted(async () => {
    await getCountries()
}) 
</script>