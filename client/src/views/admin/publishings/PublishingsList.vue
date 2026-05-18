<template>
    <div class="publishings-list">
        <div class="header">
            <h2>Издательства</h2>
            <el-button type="primary" @click="openCreateDialog">
                <el-icon><Plus /></el-icon>
                Создать
            </el-button>
        </div>

        <el-table :data="items" v-loading="loading" stripe>
            <el-table-column prop="name" label="Название" min-width="250" />
            <el-table-column prop="countryName" label="Страна" width="200" />
            <el-table-column prop="description" label="Описание" min-width="300" />
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

        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="Название" prop="name">
                    <el-input v-model="form.name" maxlength="255" show-word-limit />
                </el-form-item>
                <el-form-item label="Страна" prop="countryId">
                    <el-select v-model="form.countryId" filterable placeholder="Выберите страну" style="width: 100%">
                        <el-option v-for="c in countries" :key="c.id" :label="c.name" :value="c.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="Описание" prop="description">
                    <el-input v-model="form.description" type="textarea" :rows="3" />
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
            :message="`Удалить издательство ${toDelete?.name}?`"
            @confirm="deleteItem"
        />
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import * as publishingsApi from '@/services/api/publishings'
import { getAll as getAllCountries } from '@/services/api/dictionaries'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const items = ref([])
const countries = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({ name: '', description: '', countryId: null })
const toDelete = ref(null)
const formRef = ref()

const rules = {
    name: [{ required: true, message: 'Введите название издательства', trigger: 'blur' }],
    countryId: [{ required: true, message: 'Выберите страну', trigger: 'change' }]
}

const dialogTitle = computed(() => isEdit.value ? 'Редактировать издательство' : 'Создать издательство')

const loadCountries = async () => {
    try {
        const response = await getAllCountries('/countries')
        countries.value = response.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки стран')
    }
}

const loadItems = async () => {
    loading.value = true
    try {
        const response = await publishingsApi.getAll()
        items.value = response.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки издательств')
    } finally {
        loading.value = false
    }
}

const openCreateDialog = () => {
    isEdit.value = false
    form.value = { name: '', description: '', countryId: null }
    dialogVisible.value = true
}

const openEditDialog = (row) => {
    isEdit.value = true
    form.value = { id: row.id, name: row.name, description: row.description, countryId: row.countryId }
    dialogVisible.value = true
}

const save = async () => {
    try {
        await formRef.value?.validate()
        saving.value = true
        
        if (isEdit.value) {
            await publishingsApi.update(form.value.id, { 
                name: form.value.name, 
                description: form.value.description, 
                countryId: form.value.countryId 
            })
            ElMessage.success('Издательство обновлено')
        } else {
            await publishingsApi.create({ 
                name: form.value.name, 
                description: form.value.description, 
                countryId: form.value.countryId 
            })
            ElMessage.success('Издательство создано')
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
        await publishingsApi.deleteItem(toDelete.value.id)
        ElMessage.success('Издательство удалено')
        deleteDialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка удаления')
    }
}

onMounted(() => {
    loadCountries()
    loadItems()
})
</script>

<style scoped>
.publishings-list {
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