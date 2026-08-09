<template>
    <div class="book-card" @click="goToDetail">
        <div class="book-cover">
            <el-icon :size="48"><Reading /></el-icon>
        </div>
        <div class="book-info">
            <h3 class="book-title">{{ book.name }}</h3>
            <p class="book-author">{{ book.authorName || 'Автор не указан' }}</p>
            <div class="book-meta">
                <el-tag size="small" type="info">{{ book.genreName }}</el-tag>
                <span class="book-year">{{ book.publicationYear }}</span>
            </div>
            
            <!-- Отображение доступности -->
            <div class="book-availability">
                <el-tag 
                    :type="availabilityType" 
                    size="small"
                    :effect="availableCount > 0 ? 'light' : 'plain'"
                >
                    <el-icon v-if="availableCount > 0"><Check /></el-icon>
                    <el-icon v-else><Close /></el-icon>
                    {{ availabilityText }}
                </el-tag>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Reading, Check, Close } from '@element-plus/icons-vue'
import { getAvailableCount } from '@/services/api/books'

const props = defineProps({
    book: {
        type: Object,
        required: true
    }
})

const router = useRouter()
const availableCount = ref(0)
const loading = ref(false)

const availabilityType = computed(() => {
    if (availableCount.value === 0) return 'danger'
    if (availableCount.value <= 3) return 'warning'
    return 'success'
})

const availabilityText = computed(() => {
    if (availableCount.value === 0) return 'Нет в наличии'
    if (availableCount.value === 1) return '1 экземпляр'
    if (availableCount.value >= 2 && availableCount.value <= 4) 
        return `${availableCount.value} экземпляра`
    return `${availableCount.value} экземпляров`
})

async function fetchAvailableCount() {
    if (loading.value) return
    
    loading.value = true
    try {
        const response = await getAvailableCount(props.book.id)
        console.log(response)
        availableCount.value = response.data || 0
    } catch (error) {
        console.error('Ошибка загрузки количества доступных экземпляров', error)
        availableCount.value = 0
    } finally {
        loading.value = false
    }
}

const goToDetail = () => {
    router.push(`/book/${props.book.id}`)
}

onMounted(() => {
    fetchAvailableCount()
})
</script>

<style scoped>
.book-card {
    display: flex;
    gap: 16px;
    padding: 16px;
    background: white;
    border-radius: 12px;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
    border: 1px solid #e4e7ed;
}

.book-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.book-cover {
    width: 80px;
    height: 100px;
    background: #f5f7fa;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #909399;
    flex-shrink: 0;
}

.book-info {
    flex: 1;
    min-width: 0;
}

.book-title {
    font-size: 16px;
    font-weight: 600;
    margin: 0 0 4px 0;
    color: #2c3e50;
}

.book-author {
    font-size: 14px;
    color: #909399;
    margin: 0 0 8px 0;
}

.book-meta {
    display: flex;
    gap: 12px;
    align-items: center;
    margin-bottom: 8px;
}

.book-year {
    font-size: 13px;
    color: #c0c4cc;
}

.book-availability {
    margin-top: 4px;
}

.book-availability .el-tag {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    font-weight: 500;
}

.book-availability .el-icon {
    font-size: 14px;
}
</style>