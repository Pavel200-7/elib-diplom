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
                    @activate="handleActivate"
                />
            </div>

            <!-- Страница 2: История операций (заглушка) -->
            <div v-show="activeTab === 'history'" class="history-tab">
                <UserHistoryPlaceholder />
            </div>

            <!-- Страница 3: Выдача книг (заглушка) -->
            <div v-show="activeTab === 'issue'" class="issue-tab">
                <UserIssuePlaceholder />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUser } from '@/services/composables/useUser'

import UserInfo from '@/components/users/user-details/user-info/UserInfo.vue'
import UserHistoryPlaceholder from '@/components/users/user-details/user-history/UserHistoryPlaceholder.vue'
import UserIssuePlaceholder from '@/components/users/user-details/user-issue/UserIssue.vue'

const router = useRouter()
const route = useRoute()
const { getUser, updateUser, activateUser, user, loading } = useUser()

const activeTab = ref('info')

const goBack = () => {
    router.push({ name: 'AdminUsers' })
}

const handleTabChange = (tab) => {
    console.log('Tab changed to:', tab)
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
</style>