<!-- CopyActions.vue -->
<template>
    <div class="copy-actions">
        <!-- ADDED → AVAILABLE -->
        <el-button 
            v-if="row.status === 'ADDED'"
            type="success" 
            size="small"
            @click.stop="handleMakeAvailable"
        >
            Отметить доступной
        </el-button>
        
        <!-- IN_TRANSIT → AVAILABLE (расставить) -->
        <el-button 
            v-if="row.status === 'IN_TRANSIT'"
            type="primary" 
            size="small"
            @click.stop="handleShelve"
        >
            Расставить
        </el-button>
        
        <!-- WRITTEN_OFF нельзя списать повторно, ADDED можно удалить, остальные можно списать -->
        <el-button 
            v-if="row.status !== 'WRITTEN_OFF' && row.status !== 'ADDED'"
            type="danger" 
            size="small"
            @click.stop="handleWriteOff"
        >
            Списать
        </el-button>
        
        <!-- Редактирование (только для ADDED) -->
        <el-button 
            v-if="row.status === 'ADDED'"
            link type="primary" 
            @click.stop="handleEdit" 
            size="small"
        >
            <el-icon><Edit /></el-icon>
        </el-button>
    </div>
</template>

<script setup>
import { Edit } from '@element-plus/icons-vue'

const props = defineProps({
    row: {
        type: Object,
        required: true
    }
})

const emit = defineEmits([
    'makeAvailable', 
    'shelveCopy', 
    'writeOff', 
    'edit'
])

const handleMakeAvailable = () => {
    emit('makeAvailable', props.row.id)
}

const handleShelve = () => {
    emit('shelveCopy', props.row.id)
}

const handleWriteOff = () => {
    emit('writeOff', props.row.id)
}

const handleEdit = () => {
    emit('edit', props.row)
}
</script>

<style scoped>
.copy-actions {
    display: flex;
    gap: 4px;
    flex-wrap: wrap;
    justify-content: center;
}
</style>