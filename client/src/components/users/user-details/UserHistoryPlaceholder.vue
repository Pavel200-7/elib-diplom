<template>
    <el-card>
        <div class="placeholder-content">
            <el-empty description="История операций пользователя">
                <template #description>
                    <div class="placeholder-text">
                        <h4>История взаимодействий</h4>
                        <p>Здесь будет отображаться полная история операций пользователя:</p>
                        <ul>
                            <li>Выдача книг</li>
                            <li>Возврат книг</li>
                            <li>Продление срока выдачи</li>
                            <li>Бронирование книг</li>
                            <li>Отмена бронирования</li>
                        </ul>
                        <p class="note">Функционал находится в разработке</p>
                    </div>
                </template>
            </el-empty>
        </div>
    </el-card>
</template>

<style scoped>
.placeholder-content {
    padding: 40px 20px;
}

.placeholder-text {
    text-align: left;
    max-width: 500px;
    margin: 0 auto;
}

.placeholder-text h4 {
    color: #303133;
    margin-bottom: 16px;
}

.placeholder-text p {
    color: #606266;
    margin: 8px 0;
}

.placeholder-text ul {
    color: #606266;
    padding-left: 20px;
    margin: 12px 0;
}

.placeholder-text ul li {
    margin: 4px 0;
}

.placeholder-text .note {
    margin-top: 16px;
    padding: 8px 12px;
    background: #f4f4f5;
    border-radius: 4px;
    color: #909399;
    font-style: italic;
    text-align: center;
}
</style>