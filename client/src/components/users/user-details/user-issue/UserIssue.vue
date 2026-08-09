<template>
    <el-card>
        <div class="issue-content">
            <div class="issue-header">
                <h3>Выдача книг пользователю</h3>
                <el-button 
                    type="primary" 
                    @click="showSelector = true"
                >
                    <el-icon><Plus /></el-icon>
                    Выдать книгу
                </el-button>
            </div>

            <!-- Отображение выбранных копий -->
            <div v-if="selectedCopies.length > 0" class="selected-copies">
                <h4>Выбранные экземпляры:</h4>
                <div class="copy-list">
                    <el-tag 
                        v-for="copy in selectedCopies" 
                        :key="copy.id"
                        closable
                        @close="removeCopy(copy.id)"
                        type="success"
                        size="large"
                    >
                        ID: {{ copy.id }} - {{ copy.bookName || 'Книга' }}
                    </el-tag>
                </div>
            </div>

            <div v-else class="placeholder-content">
                <el-empty description="Нет выбранных книг">
                    <template #description>
                        <div class="placeholder-text">
                            <p>Нажмите кнопку "Выдать книгу" для выбора экземпляра</p>
                        </div>
                    </template>
                </el-empty>
            </div>

            <!-- Компонент выбора -->
            <BookCopySelector
                v-model:visible="showSelector"
                @select="handleCopySelect"
            />
        </div>
    </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import BookCopySelector from '@/components/common/form/BookCopySelector.vue'

const showSelector = ref(false)
const selectedCopies = ref([])

const handleCopySelect = (copyId) => {
    if (!selectedCopies.value.some(copy => copy.id === copyId)) {
        selectedCopies.value.push({
            id: copyId,
            bookName: `Копия ${copyId.substring(0, 8)}...`
        })
        ElMessage.success(`Экземпляр выбран для выдачи`)
    } else {
        ElMessage.warning('Этот экземпляр уже выбран')
    }
}

const removeCopy = (copyId) => {
    selectedCopies.value = selectedCopies.value.filter(copy => copy.id !== copyId)
    ElMessage.info('Экземпляр удален из списка выдачи')
}
</script>

<style scoped>
.issue-content {
    padding: 20px;
}

.issue-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.issue-header h3 {
    margin: 0;
    color: #303133;
    font-size: 18px;
}

.selected-copies {
    padding: 20px;
    background: #f5f7fa;
    border-radius: 8px;
    min-height: 80px;
}

.selected-copies h4 {
    margin: 0 0 12px 0;
    color: #606266;
    font-size: 14px;
}

.copy-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.placeholder-content {
    padding: 40px 20px;
}

.placeholder-text {
    text-align: center;
    color: #909399;
}

.placeholder-text p {
    margin: 0;
    font-size: 14px;
}
</style>