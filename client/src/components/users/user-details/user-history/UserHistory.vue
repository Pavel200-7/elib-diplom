<template>
    <el-card>
        <div class="history-content">
            <div class="history-header">
                <h4>История выдач</h4>
                <el-button 
                    size="small" 
                    type="primary" 
                    :icon="Refresh" 
                    @click="reload"
                    :loading="loading"
                    circle
                />
            </div>

            <div v-if="loading" class="loading-container">
                <el-skeleton :rows="5" animated />
            </div>

            <div v-else-if="bookings.length === 0" class="empty-container">
                <el-empty description="История выдач пуста" :image-size="100" />
            </div>

            <div v-else class="timeline-container" ref="timelineContainer">
                <div class="timeline">
                    <div 
                        v-for="(booking, index) in bookings" 
                        :key="booking.id"
                        class="timeline-item"
                        :class="{
                            'active': booking.status === 'ISSUED',
                            'closed': booking.status === 'CLOSED',
                            'overdue': isOverdue(booking)
                        }"
                    >
                        <!-- Линия времени -->
                        <div class="timeline-line">
                            <div class="timeline-dot">
                                <el-icon v-if="booking.status === 'ISSUED'" :size="14">
                                    <Loading />
                                </el-icon>
                                <el-icon v-else :size="14">
                                    <Check />
                                </el-icon>
                            </div>
                            <div v-if="index < bookings.length - 1" class="timeline-connector"></div>
                        </div>

                        <!-- Контент -->
                        <div class="timeline-content">
                            <div class="booking-card">
                                <div class="booking-header">
                                    <div class="booking-title">
                                        <span class="book-name">
                                            {{ booking.copy?.book?.name || 'Неизвестная книга' }}
                                        </span>
                                        <el-tag 
                                            :type="getStatusType(booking.status)"
                                            size="small"
                                            class="status-tag"
                                        >
                                            {{ getStatusLabel(booking.status) }}
                                        </el-tag>
                                    </div>
                                    <div class="booking-date">
                                        <span class="date-label">Выдана:</span>
                                        <span>{{ formatDate(booking.started) }}</span>
                                    </div>
                                </div>

                                <div class="booking-details">
                                    <div class="detail-item">
                                        <span class="detail-label">Инвентарный номер:</span>
                                        <span class="detail-value">{{ booking.copy?.inventoryNumber || 'Не указан' }}</span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">ISBN:</span>
                                        <span class="detail-value">{{ booking.copy?.isbn || 'Не указан' }}</span>
                                    </div>
                                    <div class="detail-item" :class="{ 'overdue-text': isOverdue(booking) }">
                                        <span class="detail-label">Дата возврата:</span>
                                        <span class="detail-value">
                                            {{ formatDate(booking.finishing) }}
                                            <el-tag v-if="isOverdue(booking)" type="danger" size="small" style="margin-left: 8px;">
                                                Просрочено
                                            </el-tag>
                                        </span>
                                    </div>
                                    <div v-if="booking.finished" class="detail-item">
                                        <span class="detail-label">Фактический возврат:</span>
                                        <span class="detail-value">{{ formatDate(booking.finished) }}</span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Пользователь:</span>
                                        <span class="detail-value">{{ booking.userEmail || 'Не указан' }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <p v-text="userId"></p>
    </el-card>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Loading, Check, Refresh } from '@element-plus/icons-vue'
import { useBooking } from '@/services/composables/useBooking'

const props = defineProps({
    userId: {
        type: String,
        required: true
    }
})

const { getUserBookingsPage, loading } = useBooking()

const bookings = ref([])
const timelineContainer = ref(null)
const isLoading = ref(false)

// Загрузка истории
const loadBookings = async () => {
    if (isLoading.value || !props.userId) return
    
    isLoading.value = true
    try {
        // Запрашиваем все выдачи пользователя
        const criteria = {
            userId: props.userId,
        }

        const data = await getUserBookingsPage(criteria)
        bookings.value = data.content || data || []
        
        // Прокручиваем к нижней части (самые новые внизу)
        await nextTick()
        scrollToBottom()
    } catch (error) {
        console.error('Ошибка при загрузке истории:', error)
        ElMessage.error('Не удалось загрузить историю выдач')
        bookings.value = []
    } finally {
        isLoading.value = false
    }
}

// Прокрутка к нижней части
const scrollToBottom = () => {
    if (timelineContainer.value) {
        timelineContainer.value.scrollTop = timelineContainer.value.scrollHeight
    }
}

// Метод для перезагрузки (будет вызываться из родителя)
const reload = () => {
    loadBookings()
}

// Проверка просрочки
const isOverdue = (booking) => {
    if (!booking.finishing || booking.status === 'CLOSED') return false
    const finishingDate = new Date(booking.finishing)
    const now = new Date()
    return finishingDate < now
}

// Форматирование даты
const formatDate = (dateString) => {
    if (!dateString) return 'Не указано'
    const date = new Date(dateString)
    return date.toLocaleString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    })
}

// Получение статуса
const getStatusType = (status) => {
    const types = {
        'ISSUED': 'warning',
        'CLOSED': 'success'
    }
    return types[status] || 'info'
}

const getStatusLabel = (status) => {
    const labels = {
        'ISSUED': 'Выдана',
        'CLOSED': 'Закрыта'
    }
    return labels[status] || status
}

// Загружаем данные при монтировании
onMounted(() => {
    loadBookings()
})

// Следим за изменениями userId
watch(() => props.userId, (newUserId) => {
    if (newUserId) {
        loadBookings()
    }
})

// Экспортируем метод для родительского компонента
defineExpose({
    reload,
    loadBookings
})
</script>

<style scoped>
.history-content {
    padding: 20px;
}

.history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    gap: 16px;
    flex-wrap: wrap;
}

.history-header h4 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
}

.loading-container {
    padding: 20px 0;
}

.empty-container {
    padding: 40px 0;
}

/* Контейнер с прокруткой */
.timeline-container {
    max-height: 600px;
    overflow-y: auto;
    padding-right: 8px;
}

.timeline-container::-webkit-scrollbar {
    width: 6px;
}

.timeline-container::-webkit-scrollbar-track {
    background: #f5f7fa;
    border-radius: 3px;
}

.timeline-container::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 3px;
}

.timeline-container::-webkit-scrollbar-thumb:hover {
    background: #c0c4cc;
}

/* Временная линия */
.timeline {
    display: flex;
    flex-direction: column;
    gap: 0;
    padding: 8px 0;
}

.timeline-item {
    display: flex;
    gap: 20px;
    padding: 0;
    position: relative;
}

/* Левая часть - линия */
.timeline-line {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 8px;
    min-width: 32px;
    flex-shrink: 0;
}

.timeline-dot {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f7fa;
    border: 2px solid #dcdfe6;
    z-index: 1;
    transition: all 0.3s ease;
}

.timeline-item.active .timeline-dot {
    background: #409eff;
    border-color: #409eff;
    color: white;
}

.timeline-item.closed .timeline-dot {
    background: #67c23a;
    border-color: #67c23a;
    color: white;
}

.timeline-item.overdue .timeline-dot {
    background: #f56c6c;
    border-color: #f56c6c;
    color: white;
}

.timeline-connector {
    width: 2px;
    flex: 1;
    min-height: 20px;
    background: #dcdfe6;
    margin: 4px 0;
}

.timeline-item.active .timeline-connector {
    background: #409eff;
}

.timeline-item.closed .timeline-connector {
    background: #67c23a;
}

.timeline-item:last-child .timeline-connector {
    display: none;
}

/* Правая часть - контент */
.timeline-content {
    flex: 1;
    padding: 8px 0;
    min-width: 0;
}

.booking-card {
    background: #f5f7fa;
    border-radius: 8px;
    padding: 16px;
    border-left: 3px solid #dcdfe6;
    transition: all 0.3s ease;
    margin-bottom: 8px;
}

.booking-card:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transform: translateX(4px);
}

.timeline-item.active .booking-card {
    border-left-color: #409eff;
}

.timeline-item.closed .booking-card {
    border-left-color: #67c23a;
}

.timeline-item.overdue .booking-card {
    border-left-color: #f56c6c;
    background: #fef0f0;
}

.booking-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 12px;
    flex-wrap: wrap;
    gap: 8px;
}

.booking-title {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
}

.book-name {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
}

.status-tag {
    flex-shrink: 0;
}

.booking-date {
    font-size: 12px;
    color: #909399;
    white-space: nowrap;
}

.booking-date .date-label {
    color: #606266;
}

.booking-details {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 6px 20px;
    font-size: 13px;
}

.detail-item {
    display: flex;
    gap: 4px;
    color: #606266;
}

.detail-label {
    color: #909399;
    flex-shrink: 0;
}

.detail-value {
    color: #303133;
    word-break: break-all;
}

.overdue-text .detail-value {
    color: #f56c6c;
    font-weight: 500;
}

/* Адаптивность */
@media (max-width: 768px) {
    .history-header {
        flex-direction: column;
        align-items: stretch;
        gap: 12px;
    }

    .booking-details {
        grid-template-columns: 1fr;
        gap: 4px;
    }

    .booking-header {
        flex-direction: column;
        align-items: flex-start;
    }

    .booking-date {
        white-space: normal;
    }

    .timeline-item {
        gap: 12px;
    }

    .timeline-content {
        padding: 4px 0;
    }

    .booking-card {
        padding: 12px;
    }
}

@media (max-width: 480px) {
    .timeline-line {
        min-width: 24px;
    }

    .timeline-dot {
        width: 22px;
        height: 22px;
    }

    .timeline-dot .el-icon {
        font-size: 12px !important;
    }

    .book-name {
        font-size: 13px;
    }
}
</style>