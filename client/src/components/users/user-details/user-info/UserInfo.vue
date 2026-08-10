<template>
    <el-row :gutter="20">
        <el-col :span="14">
            <el-card class="user-card" v-loading="loading">
                <template v-if="user">
                    <div class="user-header">
                        <h3>Персональные данные</h3>
                        <el-button 
                            v-if="!isEditing" 
                            type="primary" 
                            size="small"
                            @click="startEditing"
                        >
                            <el-icon><Edit /></el-icon>
                            Редактировать
                        </el-button>
                        <el-button 
                            v-else 
                            size="small"
                            @click="cancelEditing"
                        >
                            Отмена
                        </el-button>
                    </div>

                    <!-- Режим просмотра -->
                    <UserInfoView 
                        v-if="!isEditing" 
                        :user="user"
                    />

                    <!-- Режим редактирования -->
                    <UserInfoEdit 
                        v-else
                        ref="editFormRef"
                        :user="user"
                        @save="handleSave"
                        @cancel="cancelEditing"
                    />

                    <!-- Слот для дополнительных действий (например, кнопка активации) -->
                    <div v-if="$slots.actions" class="actions">
                        <slot name="actions" />
                    </div>
                </template>
                <template v-else-if="!loading">
                    <el-empty description="Пользователь не найден" />
                </template>
            </el-card>
        </el-col>

        <el-col :span="10">
            <UserInfoStats :user="user" />
        </el-col>
    </el-row>
</template>

<script setup>
import { ref } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import UserInfoView from './UserInfoView.vue'
import UserInfoEdit from './UserInfoEdit.vue'
import UserInfoStats from './UserInfoStats.vue'

const props = defineProps(['user', 'loading'])
const emit = defineEmits(['update'])

const isEditing = ref(false)
const editFormRef = ref(null)

const startEditing = () => {
    isEditing.value = true
}

const cancelEditing = () => {
    isEditing.value = false
    editFormRef.value?.resetForm()
}

const handleSave = async (data) => {
    await emit('update', data)
    isEditing.value = false
}
</script>

<style scoped>
.user-card {
    margin-bottom: 20px;
}

.user-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.user-header h3 {
    margin: 0;
}

.actions {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
}
</style>