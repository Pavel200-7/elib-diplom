<template>
    <div class="callback-container">
        <el-icon class="is-loading" :size="40"><Loading /></el-icon>
        <p>Выполняется вход...</p>
    </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

onMounted(() => {
    console.log('CallbackView mounted')
    
    const hash = window.location.hash.substring(1)
    const params = new URLSearchParams(hash)
    
    const accessToken = params.get('access_token')
    const refreshToken = params.get('refresh_token')
    const error = params.get('error')
    
    if (error) {
        console.error('Auth error:', error)
        ElMessage.error('Ошибка авторизации')
        router.push('/')
        return
    }
    
    if (accessToken && refreshToken) {
        console.log('Saving tokens...')
        authStore.setTokens(accessToken, refreshToken)
        
        // Запускаем планировщик автоматического обновления токена
        authStore.scheduleTokenRefresh()
        
        ElMessage.success('Вход выполнен успешно')
        
        const redirect = sessionStorage.getItem('redirectAfterLogin') || '/'
        sessionStorage.removeItem('redirectAfterLogin')
        
        router.push(redirect)
    } else {
        console.error('No tokens in URL!')
        ElMessage.error('Не получены токены авторизации')
        router.push('/')
    }
})
</script>

<style scoped>
.callback-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    gap: 16px;
}
</style>