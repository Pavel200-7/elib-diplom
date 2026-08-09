<template>
    <div class="sidebar-group">
        <div class="sidebar-group-title" @click="show = !show">
            <slot name="title"></slot>
        </div>

        <nav class="sidebar-nav">
            <TransitionGroup name="links">
                    <router-link 
                        v-if="show"
                        v-for="(item, index) in items" 
                        :key="item.path"
                        :to="item.path"
                        class="sidebar-link"
                        active-class="sidebar-link-active"
                        :style="{ transitionDelay: `${index * 20}ms` }"
                    >
                        <el-icon><component :is="item.icon" /></el-icon>
                        <span>{{ item.title }}</span>
                    </router-link>
            </TransitionGroup>
        </nav>
    </div>
</template>

<script setup>
import { ref } from 'vue'
const props = defineProps(['items'])

const show = ref(false)
</script>

<style scoped>
.sidebar-group {
    margin: auto;
    margin-bottom: 20px;
    width: 70%;
    text-align: center;
}

.sidebar-group-title {
    padding: 8px 20px;
    font-size: 16px;
    color: #b2b5bb;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.sidebar-group-title:hover {
    font-size: 18px;
    color: #7b807c;
    letter-spacing: 1px;
}

.sidebar-nav {
    display: flex;
    flex-direction: column;
    border-top: solid #b2b5bb 2px;
    border-bottom: solid #b2b5bb 2px;
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

.links-enter-active,
.links-leave-active {
  transition: all 0.5s ease;
}
.links-enter-from,
.links-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>