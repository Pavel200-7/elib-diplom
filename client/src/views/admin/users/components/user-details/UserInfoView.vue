<template>
    <el-descriptions 
        :column="1" 
        border
        class="user-descriptions"
    >
        <el-descriptions-item label="ID">
            {{ user.id }}
        </el-descriptions-item>
        <el-descriptions-item label="№ читательского билета">
            {{ user.readerBookNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="Фамилия">
            {{ user.lastName }}
        </el-descriptions-item>
        <el-descriptions-item label="Имя">
            {{ user.firstName }}
        </el-descriptions-item>
        <el-descriptions-item label="Отчество">
            {{ user.patronymic || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="Дата рождения">
            {{ user.birthDate || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="Email">
            {{ user.email }}
        </el-descriptions-item>
        <el-descriptions-item label="Телефон">
            {{ user.phone || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="Статус">
            <el-tag 
                :type="user.status === 'ACTIVATED' ? 'success' : 'info'"
                size="default"
            >
                {{ user.status === 'ACTIVATED' ? 'Активирован' : 'Создан' }}
            </el-tag>
        </el-descriptions-item>
    </el-descriptions>
</template>

<script setup>
defineProps(['user'])
</script>

<style scoped>
.user-descriptions {
    margin-bottom: 20px;
}

:deep(.el-descriptions__label) {
    width: 180px;
}

:deep(.el-descriptions__content) {
    word-break: break-all;
}
</style>