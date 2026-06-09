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
            <div class="book-actions" @click.stop>
                <el-button 
                    size="small" 
                    type="primary" 
                    :loading="reserving"
                    :disabled="availableCount === 0 || hasActiveReservation"
                    @click="handleReserve"
                >
                    {{ getButtonText() }}
                </el-button>
                <span v-if="hasActiveReservation" class="reserved-badge">
                    <el-icon><Check /></el-icon> Забронировано
                </span>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Reading, Check } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { getAvailableCount } from '@/services/api/books'
import { reserveBook, getUserReservations } from '@/services/api/circulation'

const props = defineProps({
    book: {
        type: Object,
        required: true
    }
})

const emit = defineEmits(['reserve'])

const router = useRouter()
const authStore = useAuthStore()
const reserving = ref(false)
const availableCount = ref(0)
const hasActiveReservation = ref(false)

const goToDetail = () => {
    router.push(`/book/${props.book.id}`)
}

const getButtonText = () => {
    if (reserving.value) return 'Бронирование...'
    if (hasActiveReservation.value) return 'Забронировано'
    if (availableCount.value === 0) return 'Нет в наличии'
    return 'Забронировать'
}

const loadAvailableCount = async () => {
    try {
        const response = await getAvailableCount(props.book.id)
        availableCount.value = response.data
    } catch (error) {
        console.error('Ошибка загрузки количества доступных экземпляров', error)
    }
}

const loadActiveReservation = async () => {
    const userId = authStore.user?.id
    if (!userId) return
    
    try {
        const response = await getUserReservations(userId)
        const reservations = response.data
        const active = reservations.find(r => r.copy?.book?.id === props.book.id && 
            (r.status === 'RESERVED' || r.status === 'ISSUED'))
        hasActiveReservation.value = !!active
    } catch (error) {
        console.error('Ошибка загрузки броней', error)
    }
}

const handleReserve = async () => {
    if (!authStore.isAuthenticated) {
        ElMessage.warning('Для бронирования необходимо войти')
        return
    }
    
    if (hasActiveReservation.value) {
        ElMessage.warning('У вас уже есть активная бронь или выдача на эту книгу')
        return
    }
    
    reserving.value = true
    emit('reserve', props.book.id)
    reserving.value = false
}

onMounted(() => {
    loadAvailableCount()
    if (authStore.isAuthenticated) {
        loadActiveReservation()
    }
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
}

.book-info {
    flex: 1;
}

.book-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 4px;
    color: #2c3e50;
}

.book-author {
    font-size: 14px;
    color: #909399;
    margin-bottom: 8px;
}

.book-meta {
    display: flex;
    gap: 12px;
    align-items: center;
    margin-bottom: 12px;
}

.book-year {
    font-size: 13px;
    color: #c0c4cc;
}

.book-actions {
    margin-top: 8px;
    display: flex;
    align-items: center;
    gap: 12px;
}

.reserved-badge {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #67c23a;
}
</style>