<template>
    <div class="header">
        <h2>Экземпляры книг</h2>
        <div>
            <el-button type="success" @click="handleAddBatch">
                <el-icon><DocumentAdd /></el-icon>
                Массовое создание
            </el-button>
            <el-button type="primary" @click="handleAdd">
                <el-icon><Plus /></el-icon>
                Добавить экземпляр
            </el-button>
        </div>
    </div>
</template>

<script setup>
import { Plus, DocumentAdd } from '@element-plus/icons-vue'

const emit = defineEmits(['add', 'addBatch'])

const handleAddBatch = () => {
    emit('addBatch')
}

const handleAdd = () => {
    emit('add')
}
</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.header h2 {
    margin: 0;
}
</style>