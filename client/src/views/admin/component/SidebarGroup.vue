<template>
    <div class="sidebar-group">
        <div class="sidebar-group-title">
            <slot name="title"></slot>
        </div>
        <nav class="sidebar-nav">
            <router-link 
                v-for="item in items" 
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
</template>

<script setup>
const props = defineProps(['items'])
</script>

<style scoped>
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
</style>