<template>
    <div class="user-issue">
        <el-card>
            <div class="issue-content">
                <div class="issue-header">
                    <h3>Выдача книг пользователю</h3>
                    <el-button 
                        type="primary" 
                        @click="showSelector = true"
                    >
                        <el-icon><Plus /></el-icon>
                        Выбрать экземпляр
                    </el-button>
                </div>

                <!-- Компонент поиска -->
                <CopySearch 
                    @search="handleCopyFound"
                    @clear="handleSearchClear"
                />

                <!-- Информация о выбранном экземпляре -->
                <CopyInfo 
                    v-if="selectedCopy"
                    :copy="selectedCopy"
                    :issuing="loading"
                    @issue="handleCopyIssued"
                    @clear="handleCopyClear"
                />

                <!-- Пустое состояние -->
                <div v-else class="placeholder-content">
                    <el-empty description="Нет выбранной книги">
                        <template #description>
                            <div class="placeholder-text">
                                <p>Выберите экземпляр через кнопку "Выбрать экземпляр" или найдите по инвентарному номеру</p>
                            </div>
                        </template>
                    </el-empty>
                </div>

                <!-- Компонент выбора -->
                <BookCopySelector
                    v-model:visible="showSelector"
                    @select="handleCopySelect"
                />
            </div>
        </el-card>

        <!-- Компонент активных выдач -->
        <ActiveBookings 
            :userId="userId"
            @bookingReturned="handleBookingReturned"
            ref="activeBookingsRef"
        />
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import CopySearch from '@/components/common/form/CopySearch.vue'
import CopyInfo from '@/components/common/card/CopyInfo.vue'
import BookCopySelector from '@/components/common/form/BookCopySelector.vue'
import ActiveBookings from '@/components/bookings/ActiveBookings.vue'
import { useCirculation } from '@/services/composables/useCirculation'

// Определяем пропсы
const props = defineProps({
    userId: {
        type: String,
        required: true
    }
})

const { issueCopy, loading: issuingLoading, error, loading } = useCirculation()

const showSelector = ref(false)
const selectedCopy = ref(null)
const activeBookingsRef = ref(null)
const issuing = ref(false)

// Обработка найденной копии через поиск
const handleCopyFound = (copy) => {
    selectedCopy.value = copy
}

// Обработка очистки поиска
const handleSearchClear = () => {
    selectedCopy.value = null
}

// Обработка выбора из компонента-селектора
const handleCopySelect = (copy) => {
    if (copy && copy.id) {
        selectedCopy.value = copy
        ElMessage.success('Экземпляр выбран для выдачи')
    }
}

// Обработка успешной выдачи (теперь с логикой)
const handleCopyIssued = async (copy) => {
    if (!copy) {
        ElMessage.warning('Сначала выберите экземпляр')
        return
    }

    const bookName = copy.book?.name || 'Неизвестная книга'
    const inventoryNumber = copy.inventoryNumber || 'Номер не указан'

    await ElMessageBox.confirm(
        `Вы действительно хотите выдать книгу "${bookName}" (инв. №${inventoryNumber}) пользователю?`,
        'Подтверждение выдачи',
        {
            confirmButtonText: 'Выдать',
            cancelButtonText: 'Отмена',
            type: 'warning'
        }
    )

    
    // Вызов API для выдачи книги с использованием userId из пропса
    await issueCopy(props.userId, copy.id)
    
    ElMessage.success(`Книга "${bookName}" успешно выдана пользователю`)
    
    // Очищаем выбранную копию
    selectedCopy.value = null
    
    // Обновляем список активных выдач
    if (activeBookingsRef.value) {
        activeBookingsRef.value.reload()
    }
}

// Очистка выбранной копии
const handleCopyClear = () => {
    selectedCopy.value = null
}

// Обработка возврата книги
const handleBookingReturned = (bookingId) => {
    ElMessage.success('Книга успешно возвращена')
    // Дополнительные действия при возврате
}
</script>

<style scoped>
.user-issue {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.issue-content {
    padding: 20px;
}

.issue-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.issue-header h3 {
    margin: 0;
    color: #303133;
    font-size: 18px;
}

.placeholder-content {
    padding: 40px 20px;
}

.placeholder-text {
    text-align: center;
    color: #909399;
}

.placeholder-text p {
    margin: 0;
    font-size: 14px;
}
</style>