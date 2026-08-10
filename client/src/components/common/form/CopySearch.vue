<template>
    <div class="copy-search">
        <el-form :inline="true" class="search-form" @submit.prevent="handleSearch">
            <el-form-item>
                <el-input
                    v-model="searchValue"
                    placeholder="INV-00000001"
                    clearable
                    @clear="handleClear"
                    @keyup.enter="handleSearch"
                >
                    <template #prefix>
                        <el-icon><Search /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button 
                    type="primary" 
                    @click="handleSearch"
                    :loading="loading"
                >
                    Найти экземпляр
                </el-button>
            </el-form-item>
        </el-form>
        <div class="search-hint">
            <span class="hint-text">Введите инвентарный номер в формате INV-00000001 или только цифры</span>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { useCopy } from '@/services/composables/useCopy'

const emit = defineEmits(['search', 'clear'])

const { getCopyByInventoryNumber } = useCopy()

const searchValue = ref('')
const loading = ref(false)

const handleSearch = async () => {
    if (!searchValue.value.trim()) {
        ElMessage.warning('Введите инвентарный номер')
        return
    }

    loading.value = true
    try {
        const foundCopy = await getCopyByInventoryNumber(searchValue.value.trim())
        if (foundCopy) {
            emit('search', foundCopy)
            ElMessage.success('Экземпляр найден')
        }
    } catch (error) {
        emit('search', null)
        ElMessage.warning(error.message || 'Экземпляр не найден')
    } finally {
        loading.value = false
    }
}

const handleClear = () => {
    searchValue.value = ''
    emit('clear')
}
</script>

<style scoped>
.copy-search {
    padding: 16px;
    background: #f5f7fa;
    border-radius: 8px;
}

.search-form {
    margin-bottom: 8px;
}

.search-form .el-form-item {
    margin-bottom: 0;
}

.search-hint {
    margin-top: 8px;
}

.hint-text {
    color: #909399;
    font-size: 12px;
}
</style>