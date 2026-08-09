<template>
    <el-table 
        :data="items" 
        v-loading="loading" 
        stripe
        @selection-change="handleSelectionChange"
        row-key="id"
    >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="inventoryNumber" label="Инв. номер" width="120" />
        <el-table-column prop="isbn" label="ISBN" min-width="250" />
        <el-table-column prop="status" label="Статус" width="140">
            <template #default="{ row }">
                <CopyStatusBadge 
                    :status="row.status"
                    :statuses="statuses" 
                />
            </template>
        </el-table-column>
        <el-table-column prop="holder.name" label="Место хранения" width="150">
            <template #default="{ row }">
                {{ row.holder?.name || 'Неопределено' }}
            </template>
        </el-table-column>
        
        <!-- Слот для действий -->
        <el-table-column 
            v-if="$slots.actions" 
            label="Действия" 
            width="200" 
            fixed="right"
        >
            <template #default="scope">
                <slot name="actions" :row="scope.row" />
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
import CopyStatusBadge from '@/components/common/CopyStatusBadge.vue'

const props = defineProps({
    items: {
        type: Array,
        required: true
    },
    loading: {
        type: Boolean,
        default: false
    },
    statuses: {
        type: Array,
        default: () => []
    }
})

const emit = defineEmits(['handleSelectionChange'])

const handleSelectionChange = async (rows) => {
    emit('handleSelectionChange', rows.map(row => row.id))
}
</script>