<template>
    <div class="circulation-view">
        <h1>Циркуляция книг</h1>

        <el-tabs v-model="activeTab">
            <!-- Вкладка: Доступные книги -->
            <el-tab-pane label="Доступные книги" name="available">
                <div class="toolbar">
                    <el-input
                        v-model="availableSearch"
                        placeholder="Поиск по названию или инвентарному номеру..."
                        style="width: 300px"
                        clearable
                        @keyup.enter="loadAvailable"
                    >
                        <template #append>
                            <el-button @click="loadAvailable">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </template>
                    </el-input>
                    <el-button type="primary" @click="loadAvailable">Обновить</el-button>
                </div>

                <el-table :data="availableCopies" v-loading="loadingAvailable" stripe>
                    <el-table-column prop="inventoryNumber" label="Инв. номер" width="120" />
                    <el-table-column prop="book.name" label="Название" min-width="250" />
                    <el-table-column prop="book.authorName" label="Автор" width="200" />
                    <el-table-column prop="holder.name" label="Место хранения" width="150" />
                    <el-table-column label="Действия" width="120" fixed="right">
                        <template #default="{ row }">
                            <el-button
                                type="success"
                                size="small"
                                @click="openIssueDialog(row)"
                            >
                                Выдать
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>

            <!-- Вкладка: Зарезервированные (теперь Booking) -->
            <el-tab-pane label="Зарезервированные" name="reserved">
                <div class="toolbar">
                    <el-input
                        v-model="reservedSearch"
                        placeholder="Поиск по названию..."
                        style="width: 300px"
                        clearable
                        @keyup.enter="loadReserved"
                    >
                        <template #append>
                            <el-button @click="loadReserved">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </template>
                    </el-input>
                    <el-button type="primary" @click="loadReserved">Обновить</el-button>
                </div>

                <el-table :data="reservedBookings" v-loading="loadingReserved" stripe>
                    <el-table-column prop="copy.inventoryNumber" label="Инв. номер" width="120" />
                    <el-table-column prop="copy.book.name" label="Название" min-width="250" />
                    <el-table-column prop="copy.book.authorName" label="Автор" width="200" />
                    <el-table-column label="Читатель" width="200">
                        <template #default="{ row }">
                            {{ row.userEmail || 'Неизвестно' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="created" label="Дата брони" width="160">
                        <template #default="{ row }">
                            {{ formatDate(row.created) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="Действия" width="120" fixed="right">
                        <template #default="{ row }">
                            <el-button
                                type="primary"
                                size="small"
                                @click="issueFromReservationHandler(row.id)"
                            >
                                Выдать
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>

            <!-- Вкладка: Выданные (теперь Booking) -->
            <el-tab-pane label="Выданные" name="issued">
                <div class="toolbar">
                    <el-input
                        v-model="issuedSearch"
                        placeholder="Поиск по названию..."
                        style="width: 300px"
                        clearable
                        @keyup.enter="loadIssued"
                    >
                        <template #append>
                            <el-button @click="loadIssued">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </template>
                    </el-input>
                    <el-button type="primary" @click="loadIssued">Обновить</el-button>
                </div>

                <el-table :data="issuedBookings" v-loading="loadingIssued" stripe>
                    <el-table-column prop="copy.inventoryNumber" label="Инв. номер" width="120" />
                    <el-table-column prop="copy.book.name" label="Название" min-width="250" />
                    <el-table-column prop="copy.book.authorName" label="Автор" width="200" />
                    <el-table-column label="Читатель" width="200">
                        <template #default="{ row }">
                            {{ row.userEmail || 'Неизвестно' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="started" label="Дата выдачи" width="160">
                        <template #default="{ row }">
                            {{ formatDate(row.started) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="finishing" label="Вернуть до" width="160">
                        <template #default="{ row }">
                            <span :class="{ overdue: isOverdue(row.finishing) }">
                                {{ formatDate(row.finishing) }}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="Действия" width="120" fixed="right">
                        <template #default="{ row }">
                            <el-button
                                type="warning"
                                size="small"
                                @click="returnBookHandler(row.id)"
                            >
                                Вернуть
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>

        <!-- Диалог выбора читателя при выдаче -->
        <el-dialog title="Выдача книги" v-model="issueDialogVisible" width="500px">
            <el-form label-width="100px">
                <el-form-item label="Книга">
                    <span>{{ selectedCopy?.book?.name }} ({{ selectedCopy?.inventoryNumber }})</span>
                </el-form-item>
                <el-form-item label="Читатель">
                    <el-select
                        v-model="selectedUserId"
                        filterable
                        remote
                        reserve-keyword
                        placeholder="Введите email или телефон"
                        :remote-method="searchUsersHandler"
                        :loading="searchingUsers"
                        style="width: 100%"
                    >
                        <el-option
                            v-for="user in foundUsers"
                            :key="user.id"
                            :label="`${user.email} (${user.phone || 'без телефона'})`"
                            :value="user.id"
                        />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="issueDialogVisible = false">Отмена</el-button>
                <el-button type="primary" @click="confirmIssue" :disabled="!selectedUserId">
                    Выдать
                </el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { 
    getAvailableCopies, 
    getReservations, 
    getIssued, 
    issueDirectCopy, 
    issueFromReservation, 
    returnBook 
} from '@/services/api/circulation'
import { searchUsers } from '@/services/api/users'

const activeTab = ref('available')

// Данные для вкладок
const loadingAvailable = ref(false)
const loadingReserved = ref(false)
const loadingIssued = ref(false)
const availableCopies = ref([])
const reservedBookings = ref([])   // ← теперь Booking
const issuedBookings = ref([])     // ← теперь Booking

// Поиск
const availableSearch = ref('')
const reservedSearch = ref('')
const issuedSearch = ref('')

// Диалог выдачи
const issueDialogVisible = ref(false)
const selectedCopy = ref(null)
const selectedUserId = ref(null)
const foundUsers = ref([])
const searchingUsers = ref(false)

const formatDate = (date) => {
    if (!date) return ''
    return new Date(date).toLocaleDateString('ru-RU')
}

const isOverdue = (finishingDate) => {
    if (!finishingDate) return false
    return new Date(finishingDate) < new Date()
}

// Загрузка доступных книг
const loadAvailable = async () => {
    loadingAvailable.value = true
    try {
        const response = await getAvailableCopies({
            search: availableSearch.value || undefined
        })
        console.log(response.data)
        availableCopies.value = response.data
    } catch (error) {
        console.error('Error loading available:', error)
        ElMessage.error('Ошибка загрузки доступных книг')
    } finally {
        loadingAvailable.value = false
    }
}

const loadReserved = async () => {
    loadingReserved.value = true
    try {
        const response = await getReservations({
            search: reservedSearch.value || undefined
        })
        // Добавляем created из response, если есть
        reservedBookings.value = response.data.map(booking => ({
            ...booking,
            created: booking.created || booking.createdAt
        }))
        console.log(response)

    } catch (error) {
        console.error('Error loading reservations:', error)
        ElMessage.error('Ошибка загрузки зарезервированных книг')
    } finally {
        loadingReserved.value = false
    }
}

// Загрузка выданных (Booking)
const loadIssued = async () => {
    loadingIssued.value = true
    try {
        const response = await getIssued({
            search: issuedSearch.value || undefined
        })
        issuedBookings.value = response.data
    } catch (error) {
        console.error('Error loading issued:', error)
        ElMessage.error('Ошибка загрузки выданных книг')
    } finally {
        loadingIssued.value = false
    }
}

// Поиск пользователей
const searchUsersHandler = async (query) => {
    if (!query || query.length < 2) {
        foundUsers.value = []
        return
    }
    
    searchingUsers.value = true
    try {
        const response = await searchUsers(query)
        foundUsers.value = response.data
    } catch (error) {
        console.error('Ошибка поиска пользователей', error)
    } finally {
        searchingUsers.value = false
    }
}

// Открытие диалога выдачи
const openIssueDialog = (copy) => {
    selectedCopy.value = copy
    selectedUserId.value = null
    foundUsers.value = []
    issueDialogVisible.value = true
}

// Подтверждение выдачи (прямая выдача)
const confirmIssue = async () => {
    if (!selectedUserId.value) {
        ElMessage.warning('Выберите читателя')
        return
    }
    
    if (!selectedCopy.value) {
        ElMessage.warning('Книга не выбрана')
        return
    }
    
    try {
        await issueDirectCopy(selectedUserId.value, selectedCopy.value.id)
        ElMessage.success('Книга выдана')
        issueDialogVisible.value = false
        await loadAvailable()
        await loadReserved()
        await loadIssued()
    } catch (error) {
        console.error('Error issuing copy:', error)
        ElMessage.error(error.response?.data?.message || 'Ошибка выдачи')
    }
}

// Выдача по брони (принимает bookingId)
const issueFromReservationHandler = async (bookingId) => {
    try {
        await issueFromReservation(bookingId)
        ElMessage.success('Книга выдана по брони')
        await loadAvailable()
        await loadReserved()
        await loadIssued()
    } catch (error) {
        console.error('Error issuing from reservation:', error)
        ElMessage.error(error.response?.data?.message || 'Ошибка выдачи')
    }
}

// Возврат книги (принимает bookingId)
const returnBookHandler = async (bookingId) => {
    try {
        await returnBook(bookingId)
        ElMessage.success('Книга возвращена')
        await loadAvailable()
        await loadReserved()
        await loadIssued()
    } catch (error) {
        console.error('Error returning book:', error)
        ElMessage.error(error.response?.data?.message || 'Ошибка возврата')
    }
}

watch(activeTab, (newVal) => {
    if (newVal === 'available') loadAvailable()
    else if (newVal === 'reserved') loadReserved()
    else if (newVal === 'issued') loadIssued()
})

onMounted(() => {
    loadAvailable()
})
</script>

<style scoped>
.circulation-view {
    max-width: 1400px;
    margin: 0 auto;
}

.circulation-view h1 {
    margin-bottom: 24px;
}

.toolbar {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
}

.overdue {
    color: #f56c6c;
    font-weight: bold;
}
</style>