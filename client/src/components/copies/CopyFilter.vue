<template>
    <div class="filter">
        <el-input
            v-model="filter.inventoryNumber"
            placeholder="Инв. номер"
            style="width: 150px"
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
        <el-select v-model="filter.status" placeholder="Статус" clearable style="width: 150px">
            <el-option v-for="s in statuses" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
            <el-select v-model="filter.holderId" placeholder="Хранилище" clearable style="width: 150px">
                <el-option v-for="s in holders" :key="s.id" :label="s.name" :value="s.id" />
            </el-select>
        <el-button type="primary" @click="loadItems">Применить</el-button>
        <el-button @click="resetFilters">Сбросить</el-button>
    </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { reactive } from 'vue'

const emit = defineEmits(['load', 'setFilter', 'resetFilter'])
const props = defineProps(['bookId', 'holders', 'statuses'])

const filter = reactive({
    inventoryNumber: null,
    isbn: null,
    holderId: null,
    bookId: props.bookId || null,
    status: null
})

const resetFilters = () => {
    filter.inventoryNumber = null
    filter.isbn = null
    filter.holderId = null
    filter.bookId = null
    filter.status = null
    resetFilter()
    loadItems()
}

const loadItems = async () => {
    setFilter()
    emit('load')
}

const setFilter = async () => {
    emit('setFilter', filter)
}

const resetFilter = async () => {
    emit('resetFilter')
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