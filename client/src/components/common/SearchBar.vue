<template>
    <div class="search-bar">
        <el-input
            v-model="searchQuery"
            placeholder="Поиск книг по названию..."
            size="large"
            clearable
            @keyup.enter="handleSearch"
        >
            <template #append>
                <el-button type="primary" @click="handleSearch">
                    <el-icon><Search /></el-icon> Найти
                </el-button>
            </template>
        </el-input>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()
const searchQuery = ref('')

const handleSearch = () => {
    if (searchQuery.value.trim()) {
        router.push({
            path: '/search',
            query: { q: searchQuery.value.trim() }
        })
    }
}
</script>

<style scoped>
.search-bar {
    max-width: 600px;
    margin: 0 auto;
}
</style>