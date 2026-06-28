<template>
    <div class="filter">
        <el-input
            v-model="filter.name"
            placeholder="Поиск по названию"
            style="width: 250px"
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
        <el-select v-model="filter.authorId" placeholder="Автор" clearable filterable style="width: 200px">
            <el-option v-for="a in authors" :key="a.id" :label="a.name" :value="a.id" />
        </el-select>
        <el-select v-model="filter.genreId" placeholder="Жанр" clearable style="width: 150px">
            <el-option v-for="g in genres" :key="g.id" :label="g.name" :value="g.id" />
        </el-select>
        <el-button type="primary" @click="loadItems">Применить</el-button>
        <el-button @click="resetFilters">Сбросить</el-button>
    </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { reactive } from 'vue'

const emit = defineEmits(['load', 'set-filter', 'reset-filter'])
const props = defineProps(['authors', 'genres'])

const filter = reactive({
    name: '',
    authorId: null,
    genreId: null
})

const resetFilters = () => {
    filter.name = ''
    filter.authorId = null
    filter.genreId = null
    resetFilter()
    loadItems()
}

const loadItems = async () => {
    setFilter()
    emit('load')
}

const setFilter = async () => {
    emit('set-filter', filter)
}

const resetFilter = async () => {
    emit('reset-filter')
}
</script>


<style scoped>
.filters {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
    flex-wrap: wrap;
}
</style>