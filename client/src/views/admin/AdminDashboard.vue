<template>
    <div class="admin-container">
        <aside class="admin-sidebar">

            <div class="sidebar-header">
                <h3><a href="/">Главная</a></h3>
            </div>
            <div class="sidebar-header">
                <h3>Навигация</h3>
            </div>
            
            <div class="sidebar-group">
                <div class="sidebar-group-title">Справочники</div>
                <nav class="sidebar-nav">
                    <router-link 
                        v-for="item in dictionaries" 
                        :key="item.path"
                        :to="item.path"
                        class="sidebar-link"
                        active-class="sidebar-link-active"
                    >
                        <el-icon><component :is="item.icon" /></el-icon>
                        <span>{{ item.title }}</span>
                    </router-link>
                    <router-link 
                        v-for="item in customEntities" 
                        :key="item.path"
                        :to="item.path"
                        class="sidebar-link"
                        active-class="sidebar-link-active"
                    >
                        <el-icon><component :is="item.icon" /></el-icon>
                        <span>{{ item.title }}</span>
                    </router-link>
                </nav>
            </div>
        
            
            <div class="sidebar-group">
                <div class="sidebar-group-title">Управление</div>
                <nav class="sidebar-nav">
                    <router-link to="/admin/books" class="sidebar-link" active-class="sidebar-link-active">
                        <el-icon><Notebook /></el-icon>
                        <span>Книги</span>
                    </router-link>
                    <router-link to="/admin/copies" class="sidebar-link" active-class="sidebar-link-active">
                        <el-icon><Collection /></el-icon>
                        <span>Экземпляры</span>
                    </router-link>
                </nav>
            </div>
        </aside>
        
        <main class="admin-main">
            <router-view />
        </main>
    </div>
</template>

<script setup>
import { Flag, Collection, User, OfficeBuilding, Reading, Grid, House, Tickets, Notebook } from '@element-plus/icons-vue'

// Простые справочники (используют универсальный DictionaryCrud)
const dictionaries = [
    { path: '/admin/countries', title: 'Страны', icon: 'Flag' },
    { path: '/admin/genres', title: 'Жанры', icon: 'Collection' },
    { path: '/admin/languages', title: 'Языки', icon: 'Reading' },
    { path: '/admin/literature-groups', title: 'Группы литературы', icon: 'Grid' },
    { path: '/admin/rooms', title: 'Помещения', icon: 'House' }
]

// Нетиповые справочники (имеют отдельные компоненты)
const customEntities = [
    { path: '/admin/authors', title: 'Авторы', icon: 'User' },
    { path: '/admin/publishings', title: 'Издательства', icon: 'OfficeBuilding' },
    { path: '/admin/holders', title: 'Места хранения', icon: 'Tickets' }
]
</script>

<style scoped>
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

.sidebar-group {
    margin-bottom: 20px;
}

.sidebar-group-title {
    padding: 8px 20px;
    font-size: 12px;
    color: #909399;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.sidebar-nav {
    display: flex;
    flex-direction: column;
}

.sidebar-link {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 10px 20px;
    color: #606266;
    text-decoration: none;
    transition: all 0.2s;
    font-size: 14px;
}

.sidebar-link:hover {
    background: #f5f7fa;
    color: #409eff;
}

.sidebar-link-active {
    background: #ecf5ff;
    color: #409eff;
    border-right: 3px solid #409eff;
}

.sidebar-link .el-icon {
    font-size: 18px;
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