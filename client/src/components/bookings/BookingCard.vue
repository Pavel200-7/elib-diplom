<template>
    <div 
        class="booking-card"
        :class="{ 'overdue': isOverdue }"
        @click="handleCardClick"
    >
        <!-- Верхняя полоска статуса -->
        <div class="status-bar" :class="{ 'overdue-bar': isOverdue }"></div>
        
        <div class="card-content">
            <div class="book-info">
                <div class="book-icon">
                    <el-icon :size="28"><Reading /></el-icon>
                </div>
                <div class="book-title">
                    {{ truncatedTitle }}
                </div>
                <div class="inventory-number">
                    {{ copy?.inventoryNumber || 'Номер не указан' }}
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue'
import { Reading } from '@element-plus/icons-vue'

const props = defineProps({
    booking: {
        type: Object,
        required: true
    }
})

const emit = defineEmits(['click'])

// Вычисляем просрочку
const isOverdue = computed(() => {
    if (!props.booking.finishing) return false
    const finishingDate = new Date(props.booking.finishing)
    const now = new Date()
    return finishingDate < now && props.booking.status === 'ISSUED'
})

// Получаем копию из бронирования
const copy = computed(() => props.booking.copy)

// Обрезаем название до 20 символов
const truncatedTitle = computed(() => {
    const bookName = copy.value?.book?.name || 'Неизвестная книга'
    if (bookName.length > 20) {
        return bookName.substring(0, 20) + '...'
    }
    return bookName
})

// Обработчик клика по карточке
const handleCardClick = () => {
    emit('click', props.booking)
}
</script>

<style scoped>
.booking-card {
    width: 100%;
    background: #f5f7fa;
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 1px solid #e4e7ed;
    min-height: 120px;
    display: flex;
    flex-direction: column;
}

.booking-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-color: #409eff;
}

.booking-card.overdue {
    border-color: #f56c6c;
}

.booking-card.overdue:hover {
    border-color: #f56c6c;
    box-shadow: 0 4px 12px rgba(245, 108, 108, 0.2);
}

.status-bar {
    height: 4px;
    background: #dcdfe6;
    transition: background-color 0.3s ease;
}

.status-bar.overdue-bar {
    background: #f56c6c;
}

.card-content {
    padding: 12px;
    flex: 1;
    display: flex;
    flex-direction: column;
}

.book-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 6px;
    height: 100%;
    justify-content: center;
}

.book-icon {
    color: #409eff;
    margin-bottom: 4px;
}

.book-title {
    font-size: 13px;
    font-weight: 500;
    color: #303133;
    line-height: 1.3;
    min-height: 32px;
    display: flex;
    align-items: center;
}

.inventory-number {
    font-size: 11px;
    color: #909399;
    background: #e4e7ed;
    padding: 2px 8px;
    border-radius: 4px;
    display: inline-block;
    font-family: monospace;
}
</style>