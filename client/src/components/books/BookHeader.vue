<template>
    <div class="page-header">
        <h2>Книги</h2>
        <el-button 
            type="primary" 
            @click="handleAdd"
        >
        <el-icon><Plus /></el-icon>
            Добавить книгу
        </el-button>
    </div>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'

const emit = defineEmits(['add'])

const handleAdd = () => {
    emit('add')
}
</script>

<style scoped>
.page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.page-header h2 {
    margin: 0;
}
</style>