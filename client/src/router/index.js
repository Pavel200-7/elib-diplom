import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL
const AUTH_URL = `${API_BASE_URL}/api/v1/auth/authorize`

// Лэйауты
import DefaultLayout from '@/layouts/DefaultLayout.vue'

// Страницы
import HomeView from '@/views/HomeView.vue'
import CallbackView from '@/views/CallbackView.vue'

const routes = [
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: '',
                name: 'Home',
                component: HomeView,
                meta: { public: true }
            },
            {
                path: 'callback',
                name: 'Callback',
                component: CallbackView,
                meta: { public: true }
            },
            {
                path: 'search',
                name: 'SearchResults',
                component: () => import('@/views/SearchResultsView.vue'),
                meta: { public: true }
            },
            {
                path: 'book/:id',
                name: 'BookDetail',
                component: () => import('@/views/BookDetailView.vue'),
                meta: { public: true }
            },
            {
                path: 'reader',
                name: 'ReaderView',
                component: () => import('@/views/ReaderView.vue'),
                meta: { public: true }
            }
        ]
    },
    {
        path: '/admin',
        component: () => import('@/views/admin/AdminDashboard.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN', 'MANAGER'] },
        children: [
            {
                path: '',
                redirect: '/admin/countries'
            },
            // Простые справочники
            {
                path: 'countries',
                name: 'Сountries',
                component: () => import('@/views/admin/dictionaries/Сountries.vue')
            },
            {
                path: 'genres',
                name: 'Genres',
                component: () => import('@/views/admin/dictionaries/Genres.vue')
            },
            {
                path: 'languages',
                name: 'Languages',
                component: () => import('@/views/admin/dictionaries/Languages.vue')
            },
            {
                path: 'literature-groups',
                name: 'Literature-groups',
                component: () => import('@/views/admin/dictionaries/Literature-groups.vue')
            },
            {
                path: 'rooms',
                name: 'Rooms',
                component: () => import('@/views/admin/dictionaries/Rooms.vue')
            },
            // Нетиповые справочники
            {
                path: 'authors',
                name: 'AdminAuthors',
                component: () => import('@/views/admin/dictionaries/Authors.vue')
            },
            {
                path: 'publishings',
                name: 'AdminPublishings',
                component: () => import('@/views/admin/dictionaries/Publishings.vue')
            },
            {
                path: 'holders',
                name: 'AdminHolders',
                component: () => import('@/views/admin/dictionaries/Holders.vue')
            },
            // Управление
            {
                path: 'books',
                name: 'AdminBooks',
                component: () => import('@/views/admin/books/Books.vue')
            },
            {
                path: 'copies/:id?', 
                name: 'AdminCopies',
                component: () => import('@/views/admin/copies/Copies.vue'),
                props: true 
            },
            {
                path: 'users', 
                name: 'AdminUsers',
                component: () => import('@/views/admin/users/Users.vue'),
                props: true 
            },
            {
                path: 'users/details:id', 
                name: 'AdminUsersDetails',
                component: () => import('@/views/admin/users/UserDetails.vue'),
                props: true 
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
    
    // Callback всегда пропускаем
    if (to.path === '/callback') {
        console.log('[Router] Callback route, proceeding')
        next()
        return
    }
    
    // Публичные маршруты
    if (to.meta.public) {
        console.log('[Router] Public route:', to.path)
        next()
        return
    }
    
    // Защищённые маршруты
    if (!authStore.isAuthenticated) {
        console.log('[Router] Not authenticated, redirecting to Keycloak')
        sessionStorage.setItem('redirectAfterLogin', to.fullPath)
        window.location.href = AUTH_URL
        return
    }
    
    // Проверка ролей для админки
    if (to.meta.roles && to.meta.roles.length > 0) {
        const hasRole = authStore.hasAnyRole(to.meta.roles)
        if (!hasRole) {
            console.log('[Router] No required role, redirecting to home')
            next('/')
            return
        }
    }
    
    console.log('[Router] Authenticated, proceeding to:', to.path)
    next()
})

export default router