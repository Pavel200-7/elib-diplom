<template>
    <div class="bulk-actions" v-if="selectedIds.length > 0">
        <span>Выбрано: {{ selectedIds.length }}</span>
        <el-select v-model="holderId" placeholder="Установить место хранения" clearable style="width: 200px">
            <el-option v-for="h in holders" :key="h.id" :label="h.name" :value="h.id" />
        </el-select>
        <el-button type="primary" @click="bulkSetHolder" :disabled="!holderId">
            Применить
        </el-button>
        <el-button type="danger" @click="bulkDeleteCopies" plain>
            Удалить выбранные
        </el-button>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['bulkSetHolder', 'bulkDeleteCopies'])
const props = defineProps(['holders', 'selectedIds'])

const holderId = ref(null)

const bulkSetHolder = async () => {
    emit('bulkSetHolder', holderId.value)
}

const bulkDeleteCopies = async () => {
    emit('bulkDeleteCopies')
}
</script>

<style scoped>
.bulk-actions {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 8px;
}
</style>