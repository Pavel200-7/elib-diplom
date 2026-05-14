<template>
    <header class="navbar">
        <div class="navbar-container">
            <div class="logo">
                <router-link to="/">
                    <el-icon><Reading /></el-icon>
                    <span>ELib</span>
                </router-link>
            </div>

            <div class="nav-links">
                <router-link to="/" class="nav-link">Главная</router-link>
                
                <template v-if="authStore.isAuthenticated">
                    <router-link to="/circulation" class="nav-link">Циркуляция</router-link>
                    <router-link to="/reader" class="nav-link">Мои книги</router-link>
                    
                    <template v-if="authStore.isAdmin() || authStore.isManager()">
                        <router-link to="/admin" class="nav-link admin-link">
                            <el-icon><Setting /></el-icon>
                            Админка
                        </router-link>
                    </template>
                </template>
            </div>

            <div class="user-section">
                <template v-if="authStore.isAuthenticated">
                    <span class="user-name">{{ authStore.user?.email || 'Пользователь' }}</span>
                    <el-button size="small" @click="handleLogout" type="danger" plain>
                        Выйти
                    </el-button>
                </template>
                <template v-else>
                    <el-button size="small" @click="handleLogin" type="primary">
                        Войти
                    </el-button>
                </template>
            </div>
        </div>
    </header>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import { Reading, Setting } from '@element-plus/icons-vue'

const authStore = useAuthStore()

const handleLogin = () => {
    window.location.href = 'http://localhost:8080/api/v1/auth/authorize'
}

const handleLogout = async () => {
    try {
        if (authStore.refreshToken) {
            await logout(authStore.refreshToken)
        }
    } catch (e) {
        // Даже если запрос на логаут упал, чистим локальные данные
    } finally {
        authStore.logout()
        ElMessage.success('Вы вышли из системы')
    }
}
</script>

<style scoped>
.navbar {
    background: white;
    border-bottom: 1px solid #e4e7ed;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
    position: sticky;
    top: 0;
    z-index: 100;
}

.navbar-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    height: 60px;
    max-width: 1400px;
    margin: 0 auto;
}

.logo a {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 20px;
    font-weight: 600;
    color: #409eff;
    text-decoration: none;
}

.nav-links {
    display: flex;
    gap: 24px;
}

.nav-link {
    color: #606266;
    text-decoration: none;
    font-size: 14px;
    transition: color 0.2s;
    display: flex;
    align-items: center;
    gap: 4px;
}

.nav-link:hover {
    color: #409eff;
}

.nav-link.router-link-active {
    color: #409eff;
    font-weight: 500;
}

.admin-link {
    color: #e6a23c;
}

.admin-link:hover {
    color: #f5b042;
}

.user-section {
    display: flex;
    align-items: center;
    gap: 12px;
}

.user-name {
    font-size: 14px;
    color: #606266;
}
</style>