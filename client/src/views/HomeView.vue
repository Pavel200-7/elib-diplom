<template>
    <div class="home-view">
        <div class="hero">
            <h1>ELib — электронная библиотека</h1>
            <p>Удобный поиск, бронирование и учёт книг</p>
        </div>

        <div class="search-section">
            <SearchBar />
        </div>

        <div class="actions" v-if="authStore.isAuthenticated">
            <el-row :gutter="16" justify="center">
                <el-col :span="8" v-if="authStore.isAdmin() || authStore.isManager()">
                    <router-link to="/admin" class="action-card">
                        <el-icon :size="32"><Setting /></el-icon>
                        <h3>Администрирование</h3>
                        <p>Управление книгами, экземплярами и справочниками</p>
                    </router-link>
                </el-col>
                
                <el-col :span="8">
                    <router-link to="/circulation" class="action-card">
                        <el-icon :size="32"><Refresh /></el-icon>
                        <h3>Циркуляция</h3>
                        <p>Выдача и возврат книг</p>
                    </router-link>
                </el-col>
                
                <el-col :span="8">
                    <router-link to="/reader" class="action-card">
                        <el-icon :size="32"><User /></el-icon>
                        <h3>Мои книги</h3>
                        <p>Бронирования, выданные и возвращённые книги</p>
                    </router-link>
                </el-col>
            </el-row>
        </div>

        <div class="guest-message" v-else>
            <el-alert
                title="Для доступа ко всем функциям необходимо войти"
                type="info"
                :closable="false"
                show-icon
            />
            <el-button type="primary" size="large" @click="handleLogin" style="margin-top: 20px;">
                Войти через Keycloak
            </el-button>
        </div>
    </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import SearchBar from '@/components/common/SearchBar.vue'
import { Setting, Refresh, User } from '@element-plus/icons-vue'

const authStore = useAuthStore()

const handleLogin = () => {
    window.location.href = 'http://localhost:8080/api/v1/auth/authorize'
}
</script>

<style scoped>
.home-view {
    max-width: 1200px;
    margin: 0 auto;
}

.hero {
    text-align: center;
    padding: 48px 24px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 16px;
    color: white;
    margin-bottom: 48px;
}

.hero h1 {
    font-size: 32px;
    margin-bottom: 12px;
}

.search-section {
    margin-bottom: 48px;
}

.actions {
    margin-top: 48px;
}

.action-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 24px;
    background: white;
    border-radius: 12px;
    text-decoration: none;
    color: #2c3e50;
    transition: transform 0.2s, box-shadow 0.2s;
    height: 100%;
}

.action-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.action-card .el-icon {
    margin-bottom: 16px;
    color: #409eff;
}

.action-card h3 {
    margin-bottom: 8px;
}

.action-card p {
    font-size: 14px;
    color: #909399;
}

.guest-message {
    text-align: center;
    padding: 48px;
}
</style>