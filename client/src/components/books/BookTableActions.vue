<template>
    <div class="book-actions">
        <slot name="buttons" :row="row" />
        
        <template v-if="!$slots.buttons">
            <el-button link type="primary" @click="handleEdit">
                <el-icon><Edit /></el-icon>
            </el-button>
            <el-button link type="danger" @click="handleDelete">
                <el-icon><Delete /></el-icon>
            </el-button>
        </template>
    </div>
</template>

<script setup>
import { Edit, Delete } from '@element-plus/icons-vue'

const props = defineProps({
    row: {
        type: Object,
        required: true
    }
})

const emit = defineEmits(['edit', 'delete'])

const handleEdit = () => {
    emit('edit', props.row)
}

const handleDelete = () => {
    emit('delete', props.row)
}
</script>

<style scoped>
.book-actions {
    display: flex;
    gap: 4px;
    justify-content: center;
}
</style>