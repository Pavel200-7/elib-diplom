<template>
    <div v-if="copy" class="copy-info">
        <el-descriptions title="Информация об экземпляре" :column="2" border>
            <el-descriptions-item label="Инвентарный номер">
                {{ copy.inventoryNumber }}
            </el-descriptions-item>
            <el-descriptions-item label="Статус">
                <el-tag :type="getStatusType(copy.status)">
                    {{ getStatusLabel(copy.status) }}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="ISBN">
                {{ copy.isbn || 'Не указан' }}
            </el-descriptions-item>
            <el-descriptions-item label="ID экземпляра">
                {{ copy.id }}
            </el-descriptions-item>
            <el-descriptions-item label="Название книги" :span="2">
                {{ copy.book?.name || 'Не указано' }}
            </el-descriptions-item>
            <el-descriptions-item label="Автор" :span="2">
                {{ copy.book?.authorName || 'Не указан' }}
            </el-descriptions-item>
            <el-descriptions-item label="Жанр">
                {{ copy.book?.genreName || 'Не указан' }}
            </el-descriptions-item>
            <el-descriptions-item label="Год издания">
                {{ copy.book?.publicationYear || 'Не указан' }}
            </el-descriptions-item>
            <el-descriptions-item label="Возрастное ограничение" :span="2">
                {{ getAgeRestrictionLabel(copy.book?.ageRestrictions) || 'Не указано' }}
            </el-descriptions-item>
            <el-descriptions-item label="Место хранения" :span="2">
                <div v-if="copy.holder">
                    <div><strong>Название:</strong> {{ copy.holder.name }}</div>
                    <div><strong>Тип:</strong> {{ getHolderTypeLabel(copy.holder.type) }}</div>
                    <div v-if="copy.holder.roomName">
                        <strong>Помещение:</strong> {{ copy.holder.roomName }}
                    </div>
                </div>
                <div v-else>Не указано</div>
            </el-descriptions-item>
        </el-descriptions>

        <!-- Проверка статуса перед выдачей -->
        <div v-if="!isAvailable" class="status-warning">
            <el-alert
                :title="`Экземпляр не доступен для выдачи. Текущий статус: ${getStatusLabel(copy.status)}`"
                type="warning"
                show-icon
                :closable="false"
            />
        </div>

        <div class="action-buttons">
            <el-button 
                type="danger" 
                @click="handleClear"
                plain
            >
                <el-icon><Close /></el-icon>
                Отменить выбор
            </el-button>
            <el-button 
                type="success" 
                @click="handleIssue"
                :loading="issuing"
                :disabled="!copy || issuing || !isAvailable"
            >
                <el-icon><Checked /></el-icon>
                Выдать книгу
            </el-button>
        </div>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Close, Checked } from '@element-plus/icons-vue'

const props = defineProps({
    copy: {
        type: Object,
        default: null
    },
    issuing: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['issue', 'clear'])

// Проверка доступности экземпляра для выдачи
const isAvailable = computed(() => {
    if (!props.copy) return false
    return props.copy.status === 'AVAILABLE' || props.copy.status === 'ADDED'
})

// Методы для получения статуса
const getStatusType = (status) => {
    const types = {
        'AVAILABLE': 'success',
        'ADDED': 'info',
        'IN_TRANSIT': 'warning',
        'RESERVED': 'warning',
        'ISSUED': 'danger',
        'WRITTEN_OFF': 'info'
    }
    return types[status] || 'info'
}

const getStatusLabel = (status) => {
    const labels = {
        'ADDED': 'Добавлен',
        'AVAILABLE': 'Доступен',
        'IN_TRANSIT': 'В обработке',
        'RESERVED': 'Зарезервирован',
        'ISSUED': 'Выдан',
        'WRITTEN_OFF': 'Списан'
    }
    return labels[status] || status
}

const getAgeRestrictionLabel = (restriction) => {
    const labels = {
        'ZERO_PLUS': '0+',
        'SIX_PLUS': '6+',
        'TWELVE_PLUS': '12+',
        'SIXTEEN_PLUS': '16+',
        'EIGHTEEN_PLUS': '18+'
    }
    return labels[restriction] || restriction
}

const getHolderTypeLabel = (type) => {
    const labels = {
        'SHELF': 'Полка',
        'CABINET': 'Шкаф',
        'RACK': 'Стеллаж',
        'DEPOSITORY': 'Хранилище',
        'DISPLAY': 'Витрина',
        'LOCKER': 'Шкафчик'
    }
    return labels[type] || type
}

const handleClear = () => {
    emit('clear')
}

const handleIssue = () => {
    if (!props.copy) {
        ElMessage.warning('Сначала выберите экземпляр')
        return
    }

    if (!isAvailable.value) {
        ElMessage.warning('Этот экземпляр не доступен для выдачи')
        return
    }

    // Просто эмитим событие, всю логику выдачи переносим в родителя
    emit('issue', props.copy)
}
</script>

<style scoped>
.copy-info {
    margin-top: 20px;
}

.status-warning {
    margin-top: 16px;
}

.action-buttons {
    margin-top: 20px;
    display: flex;
    gap: 12px;
    justify-content: flex-end;
}

:deep(.el-descriptions__label) {
    font-weight: 600;
    color: #303133;
}

:deep(.el-descriptions__content) {
    color: #606266;
}
</style>