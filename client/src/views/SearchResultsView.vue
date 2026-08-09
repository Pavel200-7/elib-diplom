<template>
    <div class="search-results">
        <div class="search-header">
            <h1>Результаты поиска</h1>
            <el-input
                v-model="searchQuery"
                placeholder="Поиск по названию..."
                style="width: 300px"
                clearable
                @keyup.enter="handleSearch"
            >
                <template #append>
                    <el-button @click="search">
                        <el-icon><Search /></el-icon>
                    </el-button>
                </template>
            </el-input>
        </div>

        <div class="filters-bar">
            <el-button type="primary" link @click="showAdvancedFilters = !showAdvancedFilters">
                <el-icon><Filter /></el-icon>
                Расширенный поиск
            </el-button>
        </div>

        <AdvancedFilters 
            v-if="showAdvancedFilters" 
            @apply="search"
            @reset="resetFilters"
        />

        <div v-loading="loading" class="results-grid">
            <BookCard
                v-for="book in books"
                :key="book.id"
                :book="book"
            />
            
            <div v-if="!loading && total === 0" class="empty-state">
                <el-empty description="Книги не найдены" />
            </div>
        </div>

        <Pagination
            v-if="total > 0"
            :page="page"
            :size="size"
            :total="total"
            @update:page="page = $event; loadBooks()"
            @update:size="size = $event; loadBooks()"
        />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Filter } from '@element-plus/icons-vue'

import { useBook } from '@/services/composables/useBook'

import BookCard from '@/components/common/search/BookCard.vue'
import AdvancedFilters from '@/components/common/search/AdvancedFilters.vue'
import Pagination from '@/components/common/Pagination.vue'

const {
    books,
    loading,
    page,
    total,
    size,
    getBooks,
    setFilters,
    resetFilters
} = useBook()

const route = useRoute()
const router = useRouter()

const searchQuery = ref(route.query.q || '')
const showAdvancedFilters = ref(false)

const currentFilters = ref({})

const loadBooks = async () => {
    try { 
        await getBooks()
    } catch (error) {
        ElMessage.error('Ошибка загрузки книг')
    } 
}

const handleSearch = () => {
    loadBooks()
    router.replace({
        query: { q: searchQuery.value }
    })
}

const search = (filters) => {
    currentFilters.value = filters
    const filter = {
        name: searchQuery.value || null,
        ...currentFilters.value
    }
    setFilters(filter)
    loadBooks()
}

onMounted(() => {
    if (route.query.q) {
        loadBooks()
    }
})
</script>

<style scoped>
.search-results {
    max-width: 1200px;
    margin: 0 auto;
}

.search-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.search-header h1 {
    font-size: 24px;
    font-weight: 600;
}

.filters-bar {
    margin-bottom: 20px;
    padding-bottom: 12px;
    border-bottom: 1px solid #e4e7ed;
}

.results-grid {
    display: flex;
    flex-direction: column;
    gap: 16px;
    min-height: 400px;
}

.empty-state {
    text-align: center;
    padding: 60px 0;
}
</style>