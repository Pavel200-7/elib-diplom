<template>
    <div class="active-bookings">
        <div class="bookings-header">
            <h4>Активные выдачи</h4>
            <el-badge 
                :value="bookings.length" 
                :hidden="bookings.length === 0"
                type="primary"
            />
        </div>

        <div v-if="loading" class="loading-container">
            <el-skeleton :rows="3" animated />
        </div>

        <div v-else-if="bookings.length > 0" class="bookings-grid">
            <BookingCard
                v-for="booking in bookings"
                :key="booking.id"
                :booking="booking"
                @click="handleCardClick"
            />
        </div>

        <div v-else class="empty-container">
            <el-empty description="Нет активных выдач" :image-size="80" />
        </div>

        <!-- Диалог с деталями -->
        <BookingDetails
            v-model="showDetails"
            :booking="selectedBooking"
            @return="handleReturn"
        />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useBooking } from '@/services/composables/useBooking'
import BookingCard from './BookingCard.vue'
import BookingDetails from './BookingDetails.vue'

const props = defineProps({
    userId: {
        type: String,
        required: true
    }
})

const emit = defineEmits(['bookingReturned'])

const { getActiveUserBookings, activeBookings, loading } = useBooking()

const bookings = ref([])
const showDetails = ref(false)
const selectedBooking = ref(null)

// Загрузка активных выдач
const loadActiveBookings = async () => {
    if (!props.userId) return
    
    try {
        console.log(props.userId)
        const data = await getActiveUserBookings(props.userId)
        console.log(data)

        bookings.value = data || []
    } catch (error) {
        console.error('Ошибка при загрузке активных выдач:', error)
        ElMessage.error('Не удалось загрузить активные выдачи')
        bookings.value = []
    }
}

// Обработка клика по карточке
const handleCardClick = (booking) => {
    selectedBooking.value = booking
    showDetails.value = true
}

// Обработка возврата книги
const handleReturn = (bookingId) => {
    // Удаляем возвращенную книгу из списка
    bookings.value = bookings.value.filter(b => b.id !== bookingId)
    // Эмитим событие для родительского компонента
    emit('bookingReturned', bookingId)
}

// Загружаем данные при монтировании
onMounted(() => {
    loadActiveBookings()
})

// Экспортируем метод для перезагрузки
defineExpose({
    reload: loadActiveBookings
})
</script>

<style scoped>
.active-bookings {
    margin-top: 32px;
    padding-top: 24px;
    border-top: 1px solid #e4e7ed;
}

.bookings-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
}

.bookings-header h4 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
}

.bookings-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 16px;
}

.loading-container {
    padding: 20px 0;
}

.empty-container {
    padding: 20px 0;
}

@media (max-width: 768px) {
    .bookings-grid {
        grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
        gap: 12px;
    }
}

@media (max-width: 480px) {
    .bookings-grid {
        grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
        gap: 10px;
    }
}
</style>