<template>
    <div class="dictionary-crud">
        <div class="header">
            <h2>Помещения</h2>
            <el-button type="primary" @click="openCreateDialog">
                <el-icon><Plus /></el-icon>
                Создать
            </el-button>
        </div>

        <el-table :data="items" v-loading="loading" stripe>
            <el-table-column 
                v-for="field in displayFields" 
                :key="field.key" 
                :prop="field.key" 
                :label="field.label" 
                :min-width="150"
            />
            <el-table-column label="Действия" width="150" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" @click="openEditDialog(row)">
                        <el-icon><Edit /></el-icon>
                    </el-button>
                    <el-button link type="danger" @click="confirmDelete(row)">
                        <el-icon><Delete /></el-icon>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- Диалог создания/редактирования -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
                <el-form-item 
                    v-for="field in displayFields" 
                    :key="field.key"
                    :label="field.label"
                    :prop="field.key"
                >
                    <el-input 
                        v-if="field.type === 'text'"
                        v-model="form[field.key]"
                        :maxlength="field.maxLength"
                        show-word-limit
                    />
                    <el-input 
                        v-else-if="field.type === 'textarea'"
                        v-model="form[field.key]"
                        type="textarea"
                        :rows="3"
                    />
                    <el-select 
                        v-else-if="field.type === 'select'"
                        v-model="form[field.key]"
                        placeholder="Выберите"
                        style="width: 100%"
                    >
                        <el-option 
                            v-for="opt in field.options" 
                            :key="opt.value"
                            :label="opt.label"
                            :value="opt.value"
                        />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">Отмена</el-button>
                <el-button type="primary" @click="save" :loading="saving">Сохранить</el-button>
            </template>
        </el-dialog>

        <ConfirmDialog
            v-model="deleteDialogVisible"
            title="Подтверждение удаления"
            :message="`Удалить ${toDelete?.name}?`"
            @confirm="deleteItem"
        />
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import * as api from '@/services/api/rooms.js'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const items = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({})
const toDelete = ref(null)
const formRef = ref()


const displayFields = [
    { 
        key: 'name',          
        label: 'Название',    
        type: 'text',          
        maxLength: 100
    }
]

const rules = {
    name: [                   
        { required: true, message: 'Введите название', trigger: 'blur' }
    ]
}

const dialogTitle = computed(() => isEdit.value ? "Редактировать" : "Создать")

const loadItems = async () => {    
    loading.value = true
    try {
        const response = await api.getAll()
        items.value = response.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки данных')
    } finally {
        loading.value = false
    }
}

const openCreateDialog = () => {
    isEdit.value = false
    form.value = {}
    dialogVisible.value = true
}

const openEditDialog = (row) => {
    isEdit.value = true
    form.value = { ...row }
    dialogVisible.value = true
}

const save = async () => {
    try {
        await formRef.value?.validate()
        saving.value = true
        
        const data = { ...form.value }
        
        if (isEdit.value) {
            await api.update(data.id, data)
            ElMessage.success('Обновлено')
        } else {
            await api.create(data)
            ElMessage.success('Создано')
        }
        
        dialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка сохранения')
    } finally {
        saving.value = false
    }
}

const confirmDelete = (row) => {
    toDelete.value = row
    deleteDialogVisible.value = true
}

const deleteItem = async () => {
    try {
        await api.deleteItem(toDelete.value.id)
        ElMessage.success('Удалено')
        deleteDialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка удаления')
    }
}

onMounted(() => {
    loadItems()
    console.log(items)
})
</script>

<style scoped>
.dictionary-crud {
    padding: 24px;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.header h2 {
    margin: 0;
}
</style>