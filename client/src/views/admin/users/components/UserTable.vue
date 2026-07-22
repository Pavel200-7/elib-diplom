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
            <el-table-column prop="isbn" label="isbn" min-width="250" />
            <el-table-column prop="status" label="Статус" width="140">
                <template #default="{ row }">
                    <CopyStatusBadge 
                        :status=" row.status"
                        :statuses="statuses" 
                    />
                </template>
            </el-table-column>
            <el-table-column prop="holder.name" label="Место хранения" width="150">
                <template #default="{ row }">
                    {{ row.holder?.name || 'Неопределено' }}
                </template>
            </el-table-column>
            <el-table-column label="Действия" width="200" fixed="right">
                <template #default="{ row }">
                    <el-button 
                        v-if="row.status === 'ADDED'"
                        type="success" 
                        size="small"
                        @click="makeAvailable(row.id)"
                    >
                        Отметить доступной
                    </el-button>
                    
                    <el-button 
                        v-if="row.status === 'IN_TRANSIT'"
                        type="primary" 
                        size="small"
                        @click="shelveCopy(row.id)"
                    >
                        Расставить
                    </el-button>
                    
                    <el-button 
                        v-if="row.status !== 'WRITTEN_OFF' && row.status !== 'ADDED'"
                        type="danger" 
                        size="small"
                        @click="writeOff(row.id)"
                    >
                        Списать
                    </el-button>
                    
                    <el-button 
                        v-if="row.status === 'ADDED'"
                        link type="primary" 
                        @click="openEditDialog(row)" 
                        size="small"
                    >
                        <el-icon><Edit /></el-icon>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
</template>

<script setup>
import CopyStatusBadge from '@/components/common/CopyStatusBadge.vue'

const emit = defineEmits(['makeAvailable', 'shelveCopy', 'writeOff', 'openEditDialog', 'handleSelectionChange'])
const props = defineProps(['items', 'loading', 'statuses'])

const makeAvailable = async (id) => {
    emit('makeAvailable', id)
}

const shelveCopy = async (id) => {
    emit('shelveCopy', id)
}

const writeOff = async (id) => {
    emit('writeOff', id)
}

const openEditDialog = async (row) => {
    emit('openEditDialog', row)
}

const handleSelectionChange = async (rows) => {
    emit('handleSelectionChange', rows.map(row => row.id))
}
</script>