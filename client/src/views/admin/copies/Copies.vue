<template>
    <div class="copies-list">
        <el-page-header @back="goBack" content="Экземпляры книг" />

        <CopyHeader 
            @add="openCreateCopyDialog()"
            @add-batch="openCreateCopiesDialog()"
        />

        <CopyFilter 
            :book-id="bookId"
            :holders="holders"
            :statuses="statuses"
            @set-filter="(filter) => setFilters(filter)"
            @reset-filter="resetFilters()"
            @load="handleLoad()"
        />

        <CopyBulcActions 
            :holders="holders"
            :selected-ids="selectedIds"
            @bulk-set-holder="holderId => handleBulkSetHolder(holderId)"
            @bulk-delete-copies="handleBulkDelete()"
        />

        <CopyTable 
            :items="copies"
            :loading="loading"
            :statuses="statuses"
            @make-available="id => handleSetAvailable(id)"
            @shelve-copy="id => handleSetShelved(id)"
            @write-off="id => handleSetWrittenOff(id)"
            @open-edit-dialog="row => openUpdateCopyDialog(row)"
            @handle-selection-change="rows => handleSelectionChange(rows)"
        />

        <Pagination
            v-if="total > 0"
            :page="page"
            :size="size"
            :total="total"
            @update:page="page = $event; getCopies()"
            @update:size="size = $event; getCopies()"
        />

        <CopyDialog 
            :visible="dialogVisible" 
            :book="book"
            :is-edit="isEdit"
            :data="editingCopy"
            @save="data => handleSave(data)"
            @update:visible="closeDialog()"
        />

        <BatchCopyDialog
            :visible="batchDialogVisible"
            :book="book"
            @save="handleBatchSave"
            @update:visible="batchDialogVisible = false"
        />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router' 
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

import CopyHeader from '@/components/copies/CopyHeader.vue'
import CopyFilter from '@/components/copies/CopyFilter.vue'
import CopyBulcActions from '@/components/copies/CopyBulcActions.vue'
import CopyDialog from '@/components/copies/CopyDialog.vue'
import BatchCopyDialog from '@/components/copies/BatchCopyDialog.vue'

import CopyTable from '@/components/copies/CopyTable.vue'
import Pagination from '@/components/common/Pagination.vue'

import { useCopy } from '@/services/composables/useCopy'
import { useHolder } from '@/services/composables/useHolder.js'
import { useBook } from '@/services/composables/useBook.js'

const router = useRouter()
const route = useRoute()

const selectedIds = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingCopy = ref(null)

const batchDialogVisible = ref(false)

const {
    copies,
    page,
    size,
    total,
    loading,
    statuses,
    getCopies,
    createCopy,
    createCopies,
    updateCopy,
    bulkDeleteCopies,
    setAvailable,
    setShelved,
    setWrittenOff,
    bulkSetHolder,
    setFilters,
    resetFilters,
} = useCopy()

const {
  holders,
  getHolders,
} = useHolder()

const {
  book,
  getBook,
} = useBook()


const bookId = route.params.id 

const goBack = () => {
    router.back() 
}

const handleLoad = async () => {
    setFilters({bookId: bookId})
    await getCopies()
}

const loadDictionaries = async () => {
    await Promise.all([
        getHolders(),
        getBook(bookId)
    ])
}

const openCreateCopyDialog = async () => {
    dialogVisible.value = true
    isEdit.value = false
}

const openUpdateCopyDialog = async (row) => {
    editingCopy.value = row
    isEdit.value = true

    dialogVisible.value = true
}

const closeDialog = async () => {
    dialogVisible.value = false
}

const openCreateCopiesDialog = async (data) => {
    batchDialogVisible.value = true
}

const handleSelectionChange = (ids) => {
    selectedIds.value = ids
}

const handleSave = async (data) => {
    if (isEdit.value) {
        await updateCopy(data.id, { 
            inventoryNumber: data.inventoryNumber, 
            isbn: data.isbn 
        })
        ElMessage.success('Экземпляр обновлён')
    } else {
        await createCopy(data)
        ElMessage.success('Экземпляр добавлен')
    }
    dialogVisible.value = false
}

const handleBatchSave = async (data) => {
    await createCopies(data)
    ElMessage.success('Экземпляры добавлены')
    batchDialogVisible.value = false
}

const handleBulkSetHolder = async (holderId) => {
    await bulkSetHolder(holderId, selectedIds.value)
    ElMessage.success('Постоянные места хранения установлены')

}

const handleBulkDelete = async () => {
    await ElMessageBox.confirm('Вы уверены, что хотите удалить эти копии?', 'Подтверждение', {
        confirmButtonText: 'Да',
        cancelButtonText: 'Отмена',
        type: 'warning'
    })
    await bulkDeleteCopies(selectedIds.value)
}

const handleSetAvailable = async (id) => {
    await setAvailable(id)
    ElMessage.success('Копия доступна')
}

const handleSetShelved = async (id) => {
    await setShelved(id)
    ElMessage.success('Копия расставлена')
}

const handleSetWrittenOff = async (id) => {
    await ElMessageBox.confirm('Вы уверены, что хотите списать эту копию?', 'Подтверждение', {
        confirmButtonText: 'Да',
        cancelButtonText: 'Отмена',
        type: 'warning'
    })
    await setWrittenOff(id)
    ElMessage.success('Копия списана')
}

onMounted(async () => {
    await loadDictionaries()
    await handleLoad()
})
</script>

<style scoped>
.copies-list {
    padding: 24px;
}
</style>