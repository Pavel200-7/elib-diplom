<template>
    <div class="default-layout">
        <header class="header">
            <div class="logo">
                <router-link to="/">ELib</router-link>
            </div>
            <nav class="nav">
                <router-link to="/" class="nav-link">Главная</router-link>
                <router-link to="/search" class="nav-link">Поиск</router-link>
                
                <template v-if="authStore.isAuthenticated">
                    <!-- <router-link to="/reader" class="nav-link">Мои книги</router-link> -->
                    <template v-if="authStore.isAdmin() || authStore.hasAnyRole(['MANAGER'])">
                        <router-link to="/admin" class="nav-link admin-link">Админка</router-link>
                    </template>
                    
                    <button @click="handleLogout" class="logout-btn">Выйти</button>
                </template>
                <button v-else @click="handleLogin" class="login-btn">Войти</button>
            </nav>
        </header>
        <main class="main">
            <router-view />
        </main>
    </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL
const AUTH_URL = `${API_BASE_URL}/api/v1/auth/authorize`

const handleLogin = () => {
    window.location.href = AUTH_URL
}

const handleLogout = () => {
    authStore.logout()
    ElMessage.success('Вы вышли из системы')
}
</script>

<style scoped>
.default-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 32px;
    background: white;
    border-bottom: 1px solid #e4e7ed;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
}

.logo a {
    font-size: 24px;
    font-weight: bold;
    color: #409eff;
    text-decoration: none;
}

.nav {
    display: flex;
    gap: 20px;
    align-items: center;
}

.nav-link {
    color: #606266;
    text-decoration: none;
}

.nav-link:hover {
    color: #409eff;
}

.admin-link {
    color: #e6a23c;
}

.admin-link:hover {
    color: #f5b042;
}

.login-btn, .logout-btn {
    padding: 8px 16px;
    border-radius: 6px;
    border: none;
    cursor: pointer;
    font-size: 14px;
}

.login-btn {
    background: #409eff;
    color: white;
}

.logout-btn {
    background: #f56c6c;
    color: white;
}

.main {
    flex: 1;
    padding: 24px;
    max-width: 1400px;
    margin: 0 auto;
    width: 100%;
}
</style>