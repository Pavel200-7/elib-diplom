<template>
    <div class="book-detail" v-loading="loading">
        <template v-if="book">
            <div class="book-header">
                <div class="book-cover">
                    <el-icon :size="120"><Reading /></el-icon>
                </div>
                <div class="book-info">
                    <h1>{{ book.name }}</h1>
                    <div class="meta">
                        <el-tag size="large">{{ book.genreName }}</el-tag>
                        <span>{{ book.publicationYear }}</span>
                    </div>
                    <p class="author">{{ book.authorName }}</p>
                    <p class="publisher">{{ book.publishingName }} • {{ book.languageName }}</p>
                    <p class="pages">{{ book.pages }} страниц</p>
                    <p class="age-restriction" v-if="book.ageRestrictions">
                        Возрастное ограничение: {{ getAgeRestrictionLabel(book.ageRestrictions) }}
                    </p>
                    <div class="actions">
                        <el-button 
                            type="primary" 
                            size="large"
                            :loading="reserving"
                            :disabled="availableCount === 0 || hasActiveReservation"
                            @click="handleReserve"
                        >
                            {{ getReserveButtonText() }}
                        </el-button>
                        <span v-if="availableCount === 0 && !hasActiveReservation" class="unavailable">
                            Нет доступных экземпляров
                        </span>
                        <span v-else-if="hasActiveReservation" class="warning">
                            У вас уже есть активная бронь или выдача на эту книгу
                        </span>
                        <span v-else class="available">
                            Доступно: {{ availableCount }} экз.
                        </span>
                    </div>
                    
                    <!-- Кнопка отмены брони -->
                    <div class="actions" v-if="activeReservation">
                        <el-button 
                            type="danger" 
                            size="large"
                            :loading="cancelling"
                            @click="handleCancelReservation"
                        >
                            Отменить бронь
                        </el-button>
                    </div>
                </div>
            </div>
            
            <div class="book-description" v-if="book.description">
                <h3>Описание</h3>
                <p>{{ book.description }}</p>
            </div>
        </template>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Reading } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { getById, getAvailableCount } from '@/services/api/books'
import { reserveBook, cancelReservation, getUserReservations } from '@/services/api/circulation'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const reserving = ref(false)
const cancelling = ref(false)
const book = ref(null)
const availableCount = ref(0)
const activeReservation = ref(null)
const hasActiveReservation = ref(false)

const getAgeRestrictionLabel = (value) => {
    const map = {
        ZERO_PLUS: '0+',
        SIX_PLUS: '6+',
        TWELVE_PLUS: '12+',
        SIXTEEN_PLUS: '16+',
        EIGHTEEN_PLUS: '18+'
    }
    return map[value] || value
}

const getReserveButtonText = () => {
    if (reserving.value) return 'Бронирование...'
    if (hasActiveReservation.value) return 'Уже забронировано'
    if (availableCount.value === 0) return 'Нет в наличии'
    return 'Забронировать'
}

const loadBook = async () => {
    loading.value = true
    try {
        const bookId = route.params.id
        const [bookRes, countRes] = await Promise.all([
            getById(bookId),
            getAvailableCount(bookId)
        ])
        book.value = bookRes.data
        availableCount.value = countRes.data
        
        // Загружаем активные брони пользователя для этой книги
        if (authStore.isAuthenticated) {
            await loadActiveReservation(bookId)
        }
    } catch (error) {
        ElMessage.error('Ошибка загрузки книги')
        router.push('/')
    } finally {
        loading.value = false
    }
}

const loadActiveReservation = async (bookId) => {
    const userId = authStore.user?.id
    if (!userId) return
    
    try {
        const response = await getUserReservations(userId)
        const reservations = response.data
        activeReservation.value = reservations.find(r => r.copy?.book?.id === bookId && r.status === 'RESERVED')
        hasActiveReservation.value = !!activeReservation.value
        
        // Также проверяем, есть ли ISSUED (выданные) на эту книгу
        const issuedBook = reservations.find(r => r.copy?.book?.id === bookId && r.status === 'ISSUED')
        if (issuedBook && !hasActiveReservation.value) {
            hasActiveReservation.value = true
        }
    } catch (error) {
        console.error('Ошибка загрузки броней', error)
    }
}

const handleReserve = async () => {
    if (!authStore.isAuthenticated) {
        ElMessage.warning('Для бронирования необходимо войти')
        router.push('/')
        return
    }
    
    if (hasActiveReservation.value) {
        ElMessage.warning('У вас уже есть активная бронь или выдача на эту книгу')
        return
    }
    
    reserving.value = true
    try {
        await reserveBook(authStore.user?.id, book.value.id)
        ElMessage.success('Книга успешно забронирована')
        // Обновляем данные
        await loadBook()
    } catch (error) {
        const errorMessage = error.response?.data?.message
        if (errorMessage && errorMessage.includes('активная бронь')) {
            ElMessage.warning(errorMessage)
            hasActiveReservation.value = true
        } else {
            ElMessage.error(errorMessage || 'Ошибка бронирования')
        }
    } finally {
        reserving.value = false
    }
}

const handleCancelReservation = async () => {
    if (!activeReservation.value) return
    
    cancelling.value = true
    try {
        await cancelReservation(activeReservation.value.id)
        ElMessage.success('Бронь отменена')
        activeReservation.value = null
        hasActiveReservation.value = false
        // Обновляем количество доступных экземпляров
        await loadBook()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка отмены брони')
    } finally {
        cancelling.value = false
    }
}

onMounted(() => {
    loadBook()
})
</script>

<style scoped>
.book-detail {
    max-width: 1000px;
    margin: 0 auto;
}

.book-header {
    display: flex;
    gap: 40px;
    background: white;
    padding: 32px;
    border-radius: 16px;
    margin-bottom: 32px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.book-cover {
    width: 200px;
    height: 260px;
    background: #f5f7fa;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #c0c4cc;
}

.book-info {
    flex: 1;
}

.book-info h1 {
    font-size: 28px;
    margin-bottom: 12px;
}

.meta {
    display: flex;
    gap: 16px;
    align-items: center;
    margin-bottom: 16px;
}

.author {
    font-size: 16px;
    color: #409eff;
    margin-bottom: 12px;
}

.publisher, .pages, .age-restriction {
    font-size: 14px;
    color: #909399;
    margin-bottom: 8px;
}

.actions {
    margin-top: 24px;
    display: flex;
    align-items: center;
    gap: 16px;
    flex-wrap: wrap;
}

.available {
    color: #67c23a;
    font-weight: 500;
}

.unavailable {
    color: #f56c6c;
}

.warning {
    color: #e6a23c;
    font-weight: 500;
}

.book-description {
    background: white;
    padding: 24px;
    border-radius: 12px;
}

.book-description h3 {
    margin-bottom: 12px;
    font-size: 18px;
}

.book-description p {
    line-height: 1.6;
    color: #606266;
}
</style>