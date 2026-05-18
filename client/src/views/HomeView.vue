<template>
    <div class="home">
        <div class="hero">
            <h1>ELib — Электронная библиотека</h1>
            <p>Удобный поиск, бронирование и учёт книг</p>
        </div>
        
        <div v-if="!authStore.isAuthenticated" class="guest-message">
            <el-alert
                title="Для доступа ко всем функциям необходимо войти"
                type="info"
                :closable="false"
                show-icon
            />
        </div>
        
        <div v-else class="user-info">
            <el-card>
                <template #header>
                    <span>Добро пожаловать!</span>
                </template>
                <p>Email: {{ authStore.user?.email }}</p>
                <p>Роли: {{ authStore.roles?.join(', ') }}</p>
            </el-card>
        </div>
    </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
</script>

<style scoped>
.home {
    max-width: 800px;
    margin: 0 auto;
}

.hero {
    text-align: center;
    padding: 48px 24px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 16px;
    color: white;
    margin-bottom: 32px;
}

.hero h1 {
    font-size: 32px;
    margin-bottom: 12px;
}

.guest-message {
    text-align: center;
}

.user-info {
    max-width: 400px;
    margin: 0 auto;
}
</style>