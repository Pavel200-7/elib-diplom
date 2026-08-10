<template>
    <div class="home">
        <div class="hero">
            <h1>ELib — Электронная библиотека</h1>
            <p>Удобный поиск, бронирование и учёт книг</p>
        </div>
        
        <div v-if="!isAuthenticated" class="guest-message">
            <el-alert
                title="Для доступа ко всем функциям необходимо войти"
                type="info"
                :closable="false"
                show-icon
            />
        </div>
        
        <div v-else class="info">
            <el-card>
                <template #header>
                    <span>Добро пожаловать!</span>
                </template>
                <p>Вы находитесь на главной странице нашего сайта. Здесь вы найдете общие 
                    характеристики нашего продукта, рекомендации и ответы на 
                    часто задаваемые вопросы.</p>
            </el-card>

            <el-card>
                <template #header></template>
                <p>Elib - это современное решение каталогизации библиотечного фонда, позволяющее легко и удобно 
                    отслеживать его состояние любому желающему.</p>
                <p>Если вы читатель, здесь вы сможете подыскать себе книгу в соответствии с вашими интересами, 
                    а затем указать желание осуществить пользование ею благодаря возможности оформления брони.</p>
            </el-card>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

// Computed свойства для реактивности
const isAuthenticated = computed(() => authStore.isAuthenticated)

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

.info {
    max-width: 100%;
    margin: 0 auto; 
}

.info * {
    margin-bottom: 10px; 
}

/* Исправляем опечатки */
.info p {
    margin-bottom: 10px;
}
</style>