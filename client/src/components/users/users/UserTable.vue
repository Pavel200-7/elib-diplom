<template>
    <el-table 
        :data="items" 
        v-loading="loading" 
        stripe
        row-key="id"
    >
        <el-table-column prop="readerBookNumber" label="№ читательского билета" width="160" />
        <el-table-column prop="lastName" label="Фамилия" min-width="120" />
        <el-table-column prop="firstName" label="Имя" min-width="120" />
        <el-table-column prop="patronymic" label="Отчество" min-width="120" />
        <el-table-column prop="email" label="Email" min-width="180" />
        <el-table-column prop="phone" label="Телефон" width="140" />
        <el-table-column prop="birthDate" label="Дата рождения" width="130">
            <template #default="{ row }">
                {{ row.birthDate || '-' }}
            </template>
        </el-table-column>
        <el-table-column prop="status" label="Статус" width="140">
            <template #default="{ row }">
                <el-tag 
                    :type="row.status === 'ACTIVATED' ? 'success' : 'info'"
                    size="default"
                >
                    {{ getStatusLabel(row.status) }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column label="Действия" width="200" fixed="right">
            <template #default="{ row }">
                <el-button 
                    v-if="row.status === 'CREATED'"
                    type="success" 
                    size="small"
                    @click="handleActivateUser(row.id)"
                >
                    Активировать
                </el-button>
                
                <el-button 
                    link type="primary" 
                    @click="handleOpenDetails(row)" 
                    size="small"
                >
                    <el-icon><View /></el-icon>
                    Подробнее
                </el-button>
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
import { View } from '@element-plus/icons-vue'

const emit = defineEmits(['activateUser', 'openDetails'])
const props = defineProps(['items', 'loading', 'statuses'])

const getStatusLabel = (status) => {
    const found = props.statuses?.find(s => s.value === status)
    return found ? found.label : status
}

const handleActivateUser = (id) => {
    emit('activateUser', id)
}

const handleOpenDetails = (row) => {
    emit('openDetails', row)
}
</script>