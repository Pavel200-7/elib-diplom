<template>
    <div class="page-header">
        <h2>{{ title }}</h2>
        <el-button 
            v-if="showAddButton"
            type="primary" 
            @click="handleAdd"
        >
            <el-icon><Plus /></el-icon>
            {{ addButtonText }}
        </el-button>
    </div>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'

defineProps({
    title: {
        type: String,
        required: true
    },
    addButtonText: {
        type: String,
        default: 'Добавить'
    },
    showAddButton: {
        type: Boolean,
        default: true
    }
})

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