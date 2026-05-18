<template>
    <div class="copies-list">
        <div class="header">
            <h2>Экземпляры книг</h2>
            <div>
                <el-button type="success" @click="openBatchCreateDialog">
                    <el-icon><DocumentAdd /></el-icon>
                    Массовое создание
                </el-button>
                <el-button type="primary" @click="openCreateDialog">
                    <el-icon><Plus /></el-icon>
                    Добавить экземпляр
                </el-button>
            </div>
        </div>

        <!-- Фильтры -->
        <div class="filters">
            <el-input
                v-model="filters.inventoryNumber"
                placeholder="Инв. номер"
                style="width: 150px"
                clearable
                @clear="loadItems"
                @keyup.enter="loadItems"
            >
                <template #append>
                    <el-button @click="loadItems">
                        <el-icon><Search /></el-icon>
                    </el-button>
                </template>
            </el-input>
            <el-select v-model="filters.status" placeholder="Статус" clearable style="width: 150px">
                <el-option v-for="s in statuses" :key="s.value" :label="s.label" :value="s.value" />
            </el-select>
            <el-select v-model="filters.bookId" placeholder="Книга" clearable filterable style="width: 250px">
                <el-option v-for="b in books" :key="b.id" :label="b.name" :value="b.id" />
            </el-select>
            <el-button type="primary" @click="loadItems">Применить</el-button>
            <el-button @click="resetFilters">Сбросить</el-button>
        </div>

        <!-- Массовые действия -->
        <div class="bulk-actions" v-if="selectedRows.length > 0">
            <span>Выбрано: {{ selectedRows.length }}</span>
            <el-select v-model="bulkHolderId" placeholder="Установить место хранения" clearable style="width: 200px">
                <el-option v-for="h in holders" :key="h.id" :label="h.name" :value="h.id" />
            </el-select>
            <el-button type="primary" @click="bulkSetHolder" :disabled="!bulkHolderId">
                Применить
            </el-button>
            <el-button type="danger" @click="bulkDeleteCopies" plain>
                Удалить выбранные
            </el-button>
        </div>

        <!-- Таблица -->
        <el-table 
            :data="items" 
            v-loading="loading" 
            stripe
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="inventoryNumber" label="Инв. номер" width="120" />
            <el-table-column prop="book.name" label="Книга" min-width="250">
                <template #default="{ row }">
                    <router-link :to="`/book/${row.book.id}`" class="book-link">
                        {{ row.book.name }}
                    </router-link>
                </template>
            </el-table-column>
            <el-table-column prop="book.authorName" label="Автор" width="180" />
            <el-table-column prop="status" label="Статус" width="140">
                <template #default="{ row }">
                    <CopyStatusBadge :status="row.status" />
                </template>
            </el-table-column>
            <el-table-column prop="holder?.name" label="Место хранения" width="150" />
            <el-table-column label="Действия" width="200" fixed="right">
                <template #default="{ row }">
                    <!-- ADDED → AVAILABLE -->
                    <el-button 
                        v-if="row.status === 'ADDED'"
                        type="success" 
                        size="small"
                        @click="makeAvailable(row.id)"
                    >
                        Отметить доступной
                    </el-button>
                    
                    <!-- IN_TRANSIT → AVAILABLE (расставить) -->
                    <el-button 
                        v-if="row.status === 'IN_TRANSIT'"
                        type="primary" 
                        size="small"
                        @click="shelveCopy(row.id)"
                    >
                        Расставить
                    </el-button>
                    
                    <!-- WRITTEN_OFF нельзя списать повторно, ADDED можно удалить, остальные можно списать -->
                    <el-button 
                        v-if="row.status !== 'WRITTEN_OFF' && row.status !== 'ADDED'"
                        type="danger" 
                        size="small"
                        @click="writeOff(row.id)"
                    >
                        Списать
                    </el-button>
                    
                    <!-- Редактирование (только для ADDED) -->
                    <el-button 
                        v-if="row.status === 'ADDED'"
                        link type="primary" 
                        @click="openEditDialog(row)" 
                        size="small"
                    >
                        <el-icon><Edit /></el-icon>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <Pagination
            v-if="total > 0"
            :page="page"
            :size="size"
            :total="total"
            @update:page="page = $event; loadItems()"
            @update:size="size = $event; loadItems()"
        />

        <!-- Диалог создания/редактирования -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
                <el-form-item label="Инвентарный номер" prop="inventoryNumber">
                    <el-input v-model="form.inventoryNumber" placeholder="INV-001" />
                </el-form-item>
                <el-form-item label="ISBN" prop="isbn">
                    <el-input v-model="form.isbn" placeholder="9783161484100" />
                </el-form-item>
                <el-form-item label="Книга" prop="bookId">
                    <el-select v-model="form.bookId" filterable placeholder="Выберите книгу" style="width: 100%">
                        <el-option v-for="b in books" :key="b.id" :label="b.name" :value="b.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="Место хранения" prop="holderId">
                    <el-select v-model="form.holderId" clearable filterable placeholder="Выберите место" style="width: 100%">
                        <el-option v-for="h in holders" :key="h.id" :label="h.name" :value="h.id" />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">Отмена</el-button>
                <el-button type="primary" @click="save" :loading="saving">Сохранить</el-button>
            </template>
        </el-dialog>

        <!-- Диалог массового создания -->
        <el-dialog title="Массовое создание экземпляров" v-model="batchDialogVisible" width="600px">
            <el-alert 
                title="Введите данные в формате: инвентарный номер, ISBN (каждая строка)"
                type="info"
                :closable="false"
                style="margin-bottom: 16px"
            />
            <el-input
                v-model="batchData"
                type="textarea"
                :rows="10"
                placeholder="INV-001,9783161484100&#10;INV-002,9783161484100&#10;INV-003,9783161484100"
            />
            <el-form-item label="Книга" style="margin-top: 16px">
                <el-select v-model="batchBookId" filterable placeholder="Выберите книгу" style="width: 100%">
                    <el-option v-for="b in books" :key="b.id" :label="b.name" :value="b.id" />
                </el-select>
            </el-form-item>
            <template #footer>
                <el-button @click="batchDialogVisible = false">Отмена</el-button>
                <el-button type="primary" @click="batchCreate" :loading="batchCreating">
                    Создать
                </el-button>
            </template>
        </el-dialog>

        <ConfirmDialog
            v-model="deleteDialogVisible"
            title="Подтверждение удаления"
            :message="`Удалить экземпляр ${toDelete?.inventoryNumber}?`"
            @confirm="deleteCopy"
        />
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, DocumentAdd } from '@element-plus/icons-vue'
import * as copiesApi from '@/services/api/copies'
import * as booksApi from '@/services/api/books'
import { getAll as getAllHolders } from '@/services/api/dictionaries'
import Pagination from '@/components/common/Pagination.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import CopyStatusBadge from '@/components/common/CopyStatusBadge.vue'

const items = ref([])
const loading = ref(false)
const saving = ref(false)
const batchCreating = ref(false)
const total = ref(0)
const page = ref(0)
const size = ref(20)
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({})
const toDelete = ref(null)
const formRef = ref()
const selectedRows = ref([])
const bulkHolderId = ref(null)

// Данные для массового создания
const batchData = ref('')
const batchBookId = ref(null)

// Справочники
const books = ref([])
const holders = ref([])

// Фильтры
const filters = reactive({
    inventoryNumber: '',
    status: '',
    bookId: null
})

const statuses = [
    { value: 'ADDED', label: 'Добавлен' },
    { value: 'AVAILABLE', label: 'Доступен' },
    { value: 'IN_TRANSIT', label: 'В обработке' },
    { value: 'RESERVED', label: 'Зарезервирован' },
    { value: 'ISSUED', label: 'Выдан' },
    { value: 'WRITTEN_OFF', label: 'Списан' }
]

const rules = {
    inventoryNumber: [{ required: true, message: 'Введите инвентарный номер', trigger: 'blur' }],
    bookId: [{ required: true, message: 'Выберите книгу', trigger: 'change' }]
}

const dialogTitle = computed(() => isEdit.value ? 'Редактировать экземпляр' : 'Добавить экземпляр')

const loadBooks = async () => {
    try {
        const criteria = {
            searchCriteria: {
                name: null,
                authorId: null,
                genreId: null,
                literatureGroupId: null,
                publishingId: null,
                languageId: null,
                pagesMin: null,
                pagesMax: null,
                publicationYearMin: null,
                publicationYearMax: null,
                ageRestrictions: null
            },
            sortCriteria: {
                sortBy: 'NAME',
                sortDirection: 'ASC'
            },
            pageData: {
                page: 0,
                size: 100
            }
        }
        const response = await booksApi.getAll(criteria)
        books.value = response.data.content
    } catch (error) {
        console.error('Ошибка загрузки книг', error)
    }
}

const loadHolders = async () => {
    try {
        const response = await getAllHolders('/holders')
        holders.value = response.data
    } catch (error) {
        console.error('Ошибка загрузки мест хранения', error)
    }
}

const loadItems = async () => {
    loading.value = true
    try {
        const criteria = {
            searchCriteria: {
                inventoryNumber: filters.inventoryNumber || null,
                isbn: null,
                holderId: null,
                bookId: filters.bookId || null,
                status: filters.status || null
            },
            sortCriteria: {
                sortBy: 'INVENTORY_NUMBER',
                sortDirection: 'ASC'
            },
            pageData: {
                page: page.value,
                size: size.value
            }
        }
        const response = await copiesApi.getCopiesPage(criteria)
        items.value = response.data.content
        total.value = response.data.totalElements
    } catch (error) {
        ElMessage.error('Ошибка загрузки экземпляров')
        console.error(error)
    } finally {
        loading.value = false
    }
}

const resetFilters = () => {
    filters.inventoryNumber = ''
    filters.status = ''
    filters.bookId = null
    page.value = 0
    loadItems()
}

const handleSelectionChange = (rows) => {
    selectedRows.value = rows
}

const openCreateDialog = () => {
    isEdit.value = false
    form.value = {
        inventoryNumber: '',
        isbn: '',
        bookId: null,
        holderId: null
    }
    dialogVisible.value = true
}

const openEditDialog = (row) => {
    isEdit.value = true
    form.value = {
        id: row.id,
        inventoryNumber: row.inventoryNumber,
        isbn: row.isbn,
        bookId: row.book.id,
        holderId: row.holder?.id || null
    }
    dialogVisible.value = true
}

const openBatchCreateDialog = () => {
    batchData.value = ''
    batchBookId.value = null
    batchDialogVisible.value = true
}

const save = async () => {
    try {
        await formRef.value?.validate()
        saving.value = true
        
        const data = { ...form.value }
        
        if (isEdit.value) {
            await copiesApi.update(data.id, { 
                inventoryNumber: data.inventoryNumber, 
                isbn: data.isbn 
            })
            ElMessage.success('Экземпляр обновлён')
        } else {
            await copiesApi.create(data)
            ElMessage.success('Экземпляр добавлен')
        }
        
        dialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка сохранения')
    } finally {
        saving.value = false
    }
}

const batchCreate = async () => {
    if (!batchBookId.value) {
        ElMessage.warning('Выберите книгу')
        return
    }
    
    const lines = batchData.value.split('\n').filter(l => l.trim())
    const copiesList = lines.map(line => {
        const [inventoryNumber, isbn] = line.split(',').map(s => s.trim())
        return { inventoryNumber, isbn, bookId: batchBookId.value }
    })
    
    if (copiesList.length === 0) {
        ElMessage.warning('Введите данные')
        return
    }
    
    batchCreating.value = true
    try {
        await copiesApi.batchCreate(copiesList)
        ElMessage.success(`Создано ${copiesList.length} экземпляров`)
        batchDialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка создания')
    } finally {
        batchCreating.value = false
    }
}

const makeAvailable = async (id) => {
    try {
        await copiesApi.setAvailable(id)
        ElMessage.success('Экземпляр отмечен как доступный')
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка')
    }
}

const shelveCopy = async (id) => {
    try {
        await copiesApi.setShelved(id)
        ElMessage.success('Экземпляр расставлен на место')
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка')
    }
}

const writeOff = async (id) => {
    try {
        await copiesApi.setWrittenOff(id)
        ElMessage.success('Экземпляр списан')
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка')
    }
}

const bulkSetHolder = async () => {
    if (!bulkHolderId.value || selectedRows.value.length === 0) return
    
    try {
        await copiesApi.bulkSetHolder({
            holderId: bulkHolderId.value,
            copiesId: selectedRows.value.map(c => c.id)
        })
        ElMessage.success(`Место хранения установлено для ${selectedRows.value.length} экземпляров`)
        bulkHolderId.value = null
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка')
    }
}

const confirmDelete = (row) => {
    toDelete.value = row
    deleteDialogVisible.value = true
}

const deleteCopy = async () => {
    try {
        await copiesApi.deleteItem(toDelete.value.id)
        ElMessage.success('Экземпляр удалён')
        deleteDialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка удаления')
    }
}

const bulkDeleteCopies = async () => {
    if (selectedRows.value.length === 0) return
    
    try {
        await ElMessageBox.confirm(
            `Вы действительно хотите удалить ${selectedRows.value.length} экземпляров?`,
            'Подтверждение',
            { type: 'warning' }
        )
        await copiesApi.bulkDelete(selectedRows.value.map(c => c.id))
        ElMessage.success(`Удалено ${selectedRows.value.length} экземпляров`)
        selectedRows.value = []
        await loadItems()
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error(error.response?.data?.message || 'Ошибка удаления')
        }
    }
}

onMounted(() => {
    loadBooks()
    loadHolders()
    loadItems()
})
</script>

<style scoped>
.copies-list {
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

.filters {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
    flex-wrap: wrap;
}

.bulk-actions {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 8px;
}

.book-link {
    color: #409eff;
    text-decoration: none;
}

.book-link:hover {
    text-decoration: underline;
}
</style>