<template>
    <DictionaryTable
        :title="'Группы литературы'"
        :displayFields="displayFields"
        :rules="rules"
        :items="literatureGroups"
        :loading="loading"
        @load="getLiteratureGroups"
        @create="(data) => createLiteratureGroup(data)"
        @update="(id, data) => updateLiteratureGroup(id, data)"
        @delete="(id) => deleteLiteratureGroup(id)"
    />
</template>

<script setup>
import DictionaryTable from '@/components/dictionaries/DictionaryTable.vue'
import { useLiteratureGroup } from '@/services/composables/useLiteratureGroup'

const {
  literatureGroups,
  loading,
  getLiteratureGroups,
  createLiteratureGroup,
  updateLiteratureGroup,
  deleteLiteratureGroup
} = useLiteratureGroup()

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