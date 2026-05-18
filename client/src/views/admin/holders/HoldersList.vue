<template>
    <div class="holders-list">
        <div class="header">
            <h2>Места хранения</h2>
            <el-button type="primary" @click="openCreateDialog">
                <el-icon><Plus /></el-icon>
                Создать
            </el-button>
        </div>

        <el-table :data="items" v-loading="loading" stripe>
            <el-table-column prop="name" label="Название" min-width="250" />
            <el-table-column prop="roomName" label="Помещение" width="200" />
            <el-table-column prop="type" label="Тип" width="150">
                <template #default="{ row }">
                    {{ getTypeLabel(row.type) }}
                </template>
            </el-table-column>
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

        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="Название" prop="name">
                    <el-input v-model="form.name" maxlength="255" show-word-limit />
                </el-form-item>
                <el-form-item label="Помещение" prop="roomId">
                    <el-select v-model="form.roomId" filterable placeholder="Выберите помещение" style="width: 100%">
                        <el-option v-for="r in rooms" :key="r.id" :label="r.name" :value="r.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="Тип" prop="type">
                    <el-select v-model="form.type" placeholder="Выберите тип" style="width: 100%">
                        <el-option label="Стеллаж" value="SHELF" />
                        <el-option label="Шкаф" value="CABINET" />
                        <el-option label="Книгохранилище" value="DEPOSITORY" />
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
            :message="`Удалить место хранения ${toDelete?.name}?`"
            @confirm="deleteItem"
        />
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import * as holdersApi from '@/services/api/holders'
import { getAll as getAllRooms } from '@/services/api/dictionaries'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const items = ref([])
const rooms = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({ name: '', roomId: null, type: 'SHELF' })
const toDelete = ref(null)
const formRef = ref()

const rules = {
    name: [{ required: true, message: 'Введите название', trigger: 'blur' }],
    roomId: [{ required: true, message: 'Выберите помещение', trigger: 'change' }],
    type: [{ required: true, message: 'Выберите тип', trigger: 'change' }]
}

const getTypeLabel = (type) => {
    const map = {
        SHELF: 'Стеллаж',
        CABINET: 'Шкаф',
        DEPOSITORY: 'Книгохранилище'
    }
    return map[type] || type
}

const dialogTitle = computed(() => isEdit.value ? 'Редактировать место хранения' : 'Создать место хранения')

const loadRooms = async () => {
    try {
        const response = await getAllRooms('/rooms')
        rooms.value = response.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки помещений')
    }
}

const loadItems = async () => {
    loading.value = true
    try {
        const response = await holdersApi.getAll()
        items.value = response.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки мест хранения')
    } finally {
        loading.value = false
    }
}

const openCreateDialog = () => {
    isEdit.value = false
    form.value = { name: '', roomId: null, type: 'SHELF' }
    dialogVisible.value = true
}

const openEditDialog = (row) => {
    isEdit.value = true
    form.value = { id: row.id, name: row.name, roomId: row.roomId, type: row.type }
    dialogVisible.value = true
}

const save = async () => {
    try {
        await formRef.value?.validate()
        saving.value = true
        
        if (isEdit.value) {
            await holdersApi.update(form.value.id, { 
                name: form.value.name, 
                roomId: form.value.roomId, 
                type: form.value.type 
            })
            ElMessage.success('Место хранения обновлено')
        } else {
            await holdersApi.create({ 
                name: form.value.name, 
                roomId: form.value.roomId, 
                type: form.value.type 
            })
            ElMessage.success('Место хранения создано')
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
        await holdersApi.deleteItem(toDelete.value.id)
        ElMessage.success('Место хранения удалено')
        deleteDialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка удаления')
    }
}

onMounted(() => {
    loadRooms()
    loadItems()
})
</script>

<style scoped>
.holders-list {
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