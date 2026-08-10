<template>
    <div class="reader-view">
        <!-- Навигация назад -->
        <el-page-header @back="goBack" content="Информация о читателе" />

        <!-- Переключатель страниц -->
        <el-tabs v-model="activeTab" class="tabs" @tab-change="handleTabChange">
            <el-tab-pane label="Основная информация" name="info" />
            <el-tab-pane label="История операций" name="history" />
        </el-tabs>

        <!-- Контент страниц -->
        <div class="tab-content">
            <!-- Страница 1: Основная информация -->
            <div v-show="activeTab === 'info'" class="info-tab">
                <UserInfo 
                    :user="user"
                    :loading="loading"
                    @update="handleUpdate"
                >
                    <!-- Слот для кнопки активации - оставляем пустым -->
                    <template #actions>
                        <!-- Нет кнопки активации для читателя -->
                    </template>
                </UserInfo>
            </div>

            <!-- Страница 2: История операций -->
            <div v-show="activeTab === 'history'" class="history-tab">
                <UserHistory 
                    ref="userHistoryRef"
                    :userId="userId"
                />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUser } from '@/services/composables/useUser'
import { useAuthStore } from '@/stores/auth'

import UserInfo from '@/components/users/user-details/user-info/UserInfo.vue'
import UserHistory from '@/components/users/user-details/user-history/UserHistory.vue'

const router = useRouter()
const authStore = useAuthStore()
const { getUser, updateUser, user, loading } = useUser()

const activeTab = ref('info')
const userHistoryRef = ref(null)

// Получаем userId из authStore
const userId = computed(() => authStore.user?.id)

const goBack = () => {
    router.push({ name: 'Home' }) // или путь к дашборду читателя
}

const handleTabChange = (tab) => {
    console.log('Tab changed to:', tab)
    
    // Если переключились на вкладку истории, перезагружаем данные
    if (tab === 'history' && userHistoryRef.value) {
        userHistoryRef.value.reload()
    }
}

const handleUpdate = async (data) => {
    await updateUser(data.id, {
        firstName: data.firstName,
        lastName: data.lastName,
        patronymic: data.patronymic,
        birthDate: data.birthDate
    })
    ElMessage.success('Данные пользователя обновлены')
    return true
}

// Следим за изменением activeTab
watch(activeTab, (newTab, oldTab) => {
    // Если переключились на вкладку истории, перезагружаем данные
    if (newTab === 'history' && userHistoryRef.value) {
        userHistoryRef.value.reload()
    }
})

onMounted(async () => {
    // Получаем ID пользователя из authStore
    const userId = authStore.user?.id
    if (userId) {
        await getUser(userId)
    } else {
        ElMessage.error('Не удалось получить информацию о пользователе')
    }
})
</script>

<style scoped>
.reader-view {
    padding: 24px;
}

.tabs {
    margin-top: 20px;
    margin-bottom: 20px;
}

.tab-content {
    min-height: 400px;
}

.history-tab,
.info-tab {
    animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>