<template>
    <el-dialog
        v-model="visible"
        title="Детали книговыдачи"
        width="600px"
        :close-on-click-modal="false"
    >
        <div v-if="booking" class="booking-details">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="ID выдачи">
                    {{ booking.id }}
                </el-descriptions-item>
                <el-descriptions-item label="Статус">
                    <el-tag :type="getBookingStatusType(booking.status)">
                        {{ getBookingStatusLabel(booking.status) }}
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="Инвентарный номер" :span="2">
                    {{ booking.copy?.inventoryNumber || 'Не указан' }}
                </el-descriptions-item>
                <el-descriptions-item label="Название книги" :span="2">
                    {{ booking.copy?.book?.name || 'Не указано' }}
                </el-descriptions-item>
                <el-descriptions-item label="Дата выдачи">
                    {{ formatDate(booking.started) }}
                </el-descriptions-item>
                <el-descriptions-item 
                    label="Дата возврата"
                    :class="{ 'overdue-date': isOverdue }"
                >
                    <span :style="{ color: isOverdue ? '#f56c6c' : '#303133' }">
                        {{ formatDate(booking.finishing) }}
                        <el-tag v-if="isOverdue" type="danger" size="small" style="margin-left: 8px;">
                            Просрочено
                        </el-tag>
                    </span>
                </el-descriptions-item>
                <el-descriptions-item label="Дата фактического возврата" :span="2">
                    {{ booking.finished ? formatDate(booking.finished) : 'Не возвращена' }}
                </el-descriptions-item>
                <el-descriptions-item label="Информация о пользователе" :span="2">
                    <div>
                        <div><strong>Email:</strong> {{ booking.userEmail || 'Не указан' }}</div>
                        <div><strong>ID пользователя:</strong> {{ booking.userId || 'Не указан' }}</div>
                    </div>
                </el-descriptions-item>
            </el-descriptions>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">Закрыть</el-button>
                <el-button 
                    v-if="canReturn"
                    type="primary" 
                    @click="handleReturn"
                    :loading="returning"
                >
                    Вернуть книгу
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCirculation } from '@/services/composables/useCirculation'

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    booking: {
        type: Object,
        default: null
    }
})

const emit = defineEmits(['update:modelValue', 'return'])

const { returnCopy, loading } = useCirculation()

const visible = ref(props.modelValue)
const returning = ref(false)

// Следим за изменением modelValue
watch(() => props.modelValue, (newVal) => {
    visible.value = newVal
})

// Следим за изменением visible и обновляем modelValue
watch(visible, (newVal) => {
    emit('update:modelValue', newVal)
})

// Проверяем просрочку
const isOverdue = computed(() => {
    if (!props.booking?.finishing) return false
    const finishingDate = new Date(props.booking.finishing)
    const now = new Date()
    return finishingDate < now && props.booking.status === 'ISSUED'
})

// Можно ли вернуть книгу
const canReturn = computed(() => {
    return props.booking && props.booking.status === 'ISSUED'
})

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

// Получение статуса бронирования
const getBookingStatusType = (status) => {
    const types = {
        'ISSUED': 'warning',
        'CLOSED': 'success'
    }
    return types[status] || 'info'
}

const getBookingStatusLabel = (status) => {
    const labels = {
        'ISSUED': 'Выдана',
        'CLOSED': 'Закрыта'
    }
    return labels[status] || status
}

// Обработка возврата книги
const handleReturn = async () => {
    if (!props.booking) return

    try {
        await ElMessageBox.confirm(
            `Вы действительно хотите вернуть книгу "${props.booking.copy?.book?.name || 'Неизвестная книга'}"?`,
            'Подтверждение возврата',
            {
                confirmButtonText: 'Вернуть',
                cancelButtonText: 'Отмена',
                type: 'info'
            }
        )

        returning.value = true
        
        // Вызов API для возврата книги
        await returnCopy(props.booking.id)
        
        ElMessage.success('Книга успешно возвращена')
        
        // Закрываем диалог и эмитим событие
        visible.value = false
        emit('return', props.booking.id)
        
    } catch (error) {
        if (error !== 'cancel') {
            console.error('Ошибка при возврате книги:', error)
            ElMessage.error('Ошибка при возврате книги: ' + (error.message || 'Неизвестная ошибка'))
        }
    } finally {
        returning.value = false
    }
}
</script>

<style scoped>
.booking-details {
    padding: 4px 0;
}

:deep(.el-descriptions__label) {
    font-weight: 600;
    color: #303133;
}

:deep(.el-descriptions__content) {
    color: #606266;
}

.overdue-date :deep(.el-descriptions__content) {
    font-weight: 600;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}
</style>