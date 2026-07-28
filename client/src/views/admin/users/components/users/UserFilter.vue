<template>
    <div class="filter">
        <el-input
            v-model="filter.query"
            placeholder="Поиск по ФИО, email, телефону..."
            style="width: 300px"
            clearable
            @clear="loadItems"
            @keyup.enter="loadItems"
        >
            <template #append>
                <el-button @click="loadItems">
                    <el-icon><Search /></el-icon>
                </el-button>
            </template>
        </el-input>
        <el-button type="primary" @click="loadItems">Применить</el-button>
        <el-button @click="resetFilters">Сбросить</el-button>
    </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { reactive } from 'vue'

const emit = defineEmits(['load', 'setFilter', 'resetFilter'])

const filter = reactive({
    query: ''
})

const resetFilters = () => {
    filter.query = ''
    emit('resetFilter')
    loadItems()
}

const loadItems = async () => {
    emit('setFilter', filter)
    emit('load')
}
</script>

<style scoped>
.filter {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
    flex-wrap: wrap;
}
</style>