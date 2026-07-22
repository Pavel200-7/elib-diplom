<template>
    <div class="admin-container">
        <aside class="admin-sidebar">

            <div class="sidebar-header">
                <h3><a href="/">Главная</a></h3>
            </div>
            
            <div class="sidebar-header">
                <h3>Навигация</h3>
            </div>
            
            <SidebarGroup :items="dictionaries">
                <template v-slot:title>Справочники</template>
            </SidebarGroup>
            
            <SidebarGroup :items="entities">
                <template v-slot:title>Управление</template>
            </SidebarGroup>
    
        </aside>
        
        <main class="admin-main">
            <router-view />
        </main>
    </div>
</template>

<script setup>
import SidebarGroup from './component/SidebarGroup.vue'

import { Flag, Collection, User, OfficeBuilding, Reading, Grid, House, Tickets, Notebook } from '@element-plus/icons-vue'

// Простые справочники (используют универсальный DictionaryCrud)
const dictionaries = [
    { path: '/admin/countries', title: 'Страны', icon: 'Flag' },
    { path: '/admin/genres', title: 'Жанры', icon: 'Collection' },
    { path: '/admin/languages', title: 'Языки', icon: 'Reading' },
    { path: '/admin/literature-groups', title: 'Группы литературы', icon: 'Grid' },
    { path: '/admin/rooms', title: 'Помещения', icon: 'House' },
    { path: '/admin/authors', title: 'Авторы', icon: 'User' },
    { path: '/admin/publishings', title: 'Издательства', icon: 'OfficeBuilding' },
    { path: '/admin/holders', title: 'Места хранения', icon: 'Tickets' },
]

// Основные компоненты
const entities = [
    { path: '/admin/books', title: 'Книги', icon: 'Notebook' },
    { path: '/admin/users', title: 'Пользователи', icon: 'User' },

]
</script>

<style scoped>
.sidebar-header {
    padding: 0 20px 16px 20px;
    border-bottom: 1px solid #e4e7ed;
    margin-bottom: 16px;
}

.sidebar-header h3 {
    margin: 0;
    font-size: 16px;
    color: #2c3e50;
}

.admin-container {
    display: flex;
    min-height: calc(100vh - 60px);
    gap: 24px;
}

.admin-sidebar {
    width: 260px;
    background: white;
    border-radius: 12px;
    padding: 20px 0;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    flex-shrink: 0;
}

.admin-main {
    flex: 1;
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    min-width: 0;
}
</style>