<template>
    <el-card class="info-card">
        <template #header>
            <div class="card-header">
                <span>Дополнительная информация</span>
            </div>
        </template>
        <div class="info-placeholder">
            <el-empty 
                description="Здесь будет отображаться статистика пользователя"
                :image-size="80"
            >
                <template #description>
                    <div class="stats-placeholder">
                        <!-- <p>Количество книг на руках: <strong>0</strong></p>
                        <p>Всего выдано книг: <strong>0</strong></p>
                        <p>Активных броней: <strong>0</strong></p> -->
                        <p>Дата регистрации: <strong>{{ user?.createdAt || '-' }}</strong></p>
                    </div>
                </template>
            </el-empty>
        </div>
    </el-card>
</template>

<script setup>
defineProps(['user'])
</script>

<style scoped>
.info-card {
    height: 100%;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.stats-placeholder {
    text-align: left;
    padding: 10px 0;
}

.stats-placeholder p {
    margin: 8px 0;
    color: #606266;
}

.info-placeholder {
    padding: 10px 0;
}

:deep(.el-empty__description) {
    width: 100%;
}
</style>