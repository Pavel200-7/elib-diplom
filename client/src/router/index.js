import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// Лэйауты
import DefaultLayout from '@/layouts/DefaultLayout.vue'

// Страницы
import HomeView from '@/views/HomeView.vue'

const routes = [
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: '',
                name: 'Home',
                component: HomeView
            },
            {
                path: '/callback',
                name: 'Callback',
                component: () => import('@/views/CallbackView.vue'),
                meta: { public: true }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// Глобальный guard для авторизации
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()
    
    // ✅ Публичные маршруты пропускаем без проверки
    if (to.path === '/callback' || to.meta.public) {  // ← явно добавим /callback
        next()
        return
    }
    
    if (!authStore.isAuthenticated) {
        sessionStorage.setItem('redirectAfterLogin', to.fullPath)
        window.location.href = 'http://localhost:8085/api/v1/auth/authorize'
        return
    }
    
    next()
    return
})

export default router