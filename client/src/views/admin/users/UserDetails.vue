<template>
    <div class="user-details">
        <!-- Навигация назад -->
        <el-page-header @back="goBack" content="Детали пользователя" />

        <!-- Переключатель страниц -->
        <el-tabs v-model="activeTab" class="tabs" @tab-change="handleTabChange">
            <el-tab-pane label="Основная информация" name="info" />
            <el-tab-pane label="История операций" name="history" />
            <el-tab-pane label="Выдача книг" name="issue" />
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
                    <!-- Слот для кнопки активации -->
                    <template #actions>
                        <div v-if="user?.status === 'CREATED'" class="actions">
                            <el-button 
                                type="success" 
                                @click="handleActivate"
                            >
                                Активировать пользователя
                            </el-button>
                        </div>
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

            <!-- Страница 3: Выдача книг -->
            <div v-show="activeTab === 'issue'" class="issue-tab">
                <UserIssue :userId="userId" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUser } from '@/services/composables/useUser'

import UserInfo from '@/components/users/user-details/user-info/UserInfo.vue'
import UserHistory from '@/components/users/user-details/user-history/UserHistory.vue'
import UserIssue from '@/components/users/user-details/user-issue/UserIssue.vue'

const router = useRouter()
const route = useRoute()
const { getUser, updateUser, activateUser, user, loading } = useUser()

const activeTab = ref('info')
const userHistoryRef = ref(null)

// Получаем userId из параметров маршрута
const userId = computed(() => route.params.id)

const goBack = () => {
    router.push({ name: 'AdminUsers' })
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

const handleActivate = async () => {
    await activateUser(user.value.id)
    ElMessage.success('Пользователь активирован')
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
    const id = route.params.id
    if (id) {
        await getUser(id)
    }
})
</script>

<style scoped>
.user-details {
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
.issue-tab,
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

.actions {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
}
</style>