<template>
    <div class="reader-view">
        <h1>Мои книги</h1>

        <el-tabs v-model="activeTab">
            <el-tab-pane label="Зарезервированные" name="reserved">
                <el-table 
                    :data="reservations" 
                    v-loading="loadingReserved" 
                    stripe
                    @row-click="goToBookDetail"
                    style="cursor: pointer"
                >
                    <el-table-column label="Название" min-width="250">
                        <template #default="{ row }">
                            {{ row.copy?.book?.name || 'Название не указано' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Автор" width="200">
                        <template #default="{ row }">
                            {{ row.copy?.book?.authorName || 'Автор не указан' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Действия" width="120" fixed="right">
                        <template #default="{ row }">
                            <el-button
                                type="danger"
                                size="small"
                                :loading="cancellingId === row.id"
                                @click.stop="cancelReservation(row.id)"
                            >
                                Отменить
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>

            <el-tab-pane label="Выданные" name="issued">
                <el-table 
                    :data="issued" 
                    v-loading="loadingIssued" 
                    stripe
                    @row-click="goToBookDetail"
                    style="cursor: pointer"
                >
                    <el-table-column label="Название" min-width="250">
                        <template #default="{ row }">
                            {{ row.copy?.book?.name || 'Название не указано' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Автор" width="200">
                        <template #default="{ row }">
                            {{ row.copy?.book?.authorName || 'Автор не указан' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Дата выдачи" width="160">
                        <template #default="{ row }">
                            {{ formatDate(row.started) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Вернуть до" width="160">
                        <template #default="{ row }">
                            <span :class="{ overdue: isOverdue(row.finishing) }">
                                {{ formatDate(row.finishing) }}
                            </span>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>

            <el-tab-pane label="Возвращённые" name="history">
                <el-table 
                    :data="history" 
                    v-loading="loadingHistory" 
                    stripe
                    @row-click="goToBookDetail"
                    style="cursor: pointer"
                >
                    <el-table-column label="Название" min-width="250">
                        <template #default="{ row }">
                            {{ row.copy?.book?.name || 'Название не указано' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Автор" width="200">
                        <template #default="{ row }">
                            {{ row.copy?.book?.authorName || 'Автор не указан' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Дата возврата" width="160">
                        <template #default="{ row }">
                            {{ formatDate(row.finished) }}
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { 
    getUserReservations, 
    getUserIssued, 
    getUserHistory, 
    cancelReservation as cancelReservationApi
} from '@/services/api/circulation'

const router = useRouter()
const authStore = useAuthStore()

const activeTab = ref('reserved')
const loadingReserved = ref(false)
const loadingIssued = ref(false)
const loadingHistory = ref(false)
const reservations = ref([])
const issued = ref([])
const history = ref([])
const cancellingId = ref(null)

const formatDate = (date) => {
    if (!date) return ''
    return new Date(date).toLocaleDateString('ru-RU')
}

const isOverdue = (finishingDate) => {
    if (!finishingDate) return false
    return new Date(finishingDate) < new Date()
}

const goToBookDetail = (row) => {
    const bookId = row.copy?.book?.id
    if (bookId) {
        router.push(`/book/${bookId}`)
    }
}

const loadReservations = async () => {
    const userId = authStore.user?.id
    if (!userId) return
    
    loadingReserved.value = true
    try {
        const response = await getUserReservations(userId)
        console.log('Reservations response:', response.data)
        reservations.value = response.data
    } catch (error) {
        console.error('Error loading reservations:', error)
        ElMessage.error('Ошибка загрузки броней')
    } finally {
        loadingReserved.value = false
    }
}

const loadIssued = async () => {
    const userId = authStore.user?.id
    if (!userId) return
    
    loadingIssued.value = true
    try {
        const response = await getUserIssued(userId)
        console.log('Issued response:', response.data)
        issued.value = response.data
    } catch (error) {
        console.error('Error loading issued:', error)
        ElMessage.error('Ошибка загрузки выданных книг')
    } finally {
        loadingIssued.value = false
    }
}

const loadHistory = async () => {
    const userId = authStore.user?.id
    if (!userId) return
    
    loadingHistory.value = true
    try {
        const response = await getUserHistory(userId)
        console.log('History response:', response.data)
        history.value = response.data
    } catch (error) {
        console.error('Error loading history:', error)
        ElMessage.error('Ошибка загрузки истории')
    } finally {
        loadingHistory.value = false
    }
}

const cancelReservation = async (bookingId) => {
    cancellingId.value = bookingId
    try {
        await cancelReservationApi(bookingId)
        ElMessage.success('Бронь отменена')
        await loadReservations()
    } catch (error) {
        console.error('Error cancelling reservation:', error)
        ElMessage.error(error.response?.data?.message || 'Ошибка отмены')
    } finally {
        cancellingId.value = null
    }
}

watch(activeTab, (newVal) => {
    if (newVal === 'reserved') loadReservations()
    else if (newVal === 'issued') loadIssued()
    else if (newVal === 'history') loadHistory()
})

onMounted(() => {
    if (authStore.isAuthenticated) {
        loadReservations()
    }
})
</script>

<style scoped>
.reader-view {
    max-width: 1200px;
    margin: 0 auto;
}

.reader-view h1 {
    margin-bottom: 24px;
}

.overdue {
    color: #f56c6c;
    font-weight: bold;
}

.el-table__row {
    cursor: pointer;
}
</style>