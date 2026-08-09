<template>
    <div class="books-list">
        <BookHeader @add="openCreateDialog"/>

        <BookFilter 
            :authors="authors"
            :genres="genres"
            @set-filter="(filter) => setFilters(filter)"
            @reset-filter="resetFilters()"
            @load="getBooks()"
        />

        <BookTable 
            :books="books"
            :loading="loading"
            :row-clickable="true"
            @row-click="handleRowClick"
        >  
            <template #actions="{ row }">
                <BookTableActions 
                    :row="row"
                    @edit="openEditDialog"
                    @delete="confirmDelete"
                />
            </template>
        </BookTable>

        <Pagination
            v-if="total > 0"
            :page="page"
            :size="size"
            :total="total"
            @update:page="page = $event; getBooks()"
            @update:size="size = $event; getBooks()"
        />

        <BookDialog
            v-model:visible="dialogVisible"
            :is-edit="isEdit"
            :initial-data="selectedBook"
            :authors="authors"
            :genres="genres"
            :publishings="publishings"
            :languages="languages"
            :literature-groups="literatureGroups"
            @save="handleDialogSave"
            @close="dialogVisible = false"
        />

        <BookDeleteConfirmDialog
            v-model="deleteDialogVisible"
            :item-name="toDelete?.name"
            item-type="книгу"
            @confirm="deleteItem"
        />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'


import BookHeader from '@/components/books/BookHeader.vue'
import BookFilter from '@/components/books/BookFilter.vue'
import Pagination from '@/components/common/Pagination.vue'
import BookDialog from '@/components/books/BookDialog.vue'
import BookDeleteConfirmDialog from '@/components/books/BookDeleteConfirmDialog.vue'
import BookTable from '@/components/books/BookTable.vue'
import BookTableActions from '@/components/books/BookTableActions.vue'

import { useAuthor } from '@/services/composables/useAuthor'
import { useGenre } from '@/services/composables/useGenre'
import { usePublishing } from '@/services/composables/usePublishing'
import { useLanguage } from '@/services/composables/useLanguage'
import { useLiteratureGroup } from '@/services/composables/useLiteratureGroup.js'
import { useBook } from '@/services/composables/useBook'

const router = useRouter()


const {
  books,
  page,
  size,
  total,
  loading,
  getBooks,
  createBook,
  updateBook,
  deleteBook,
  setFilters,
  resetFilters
} = useBook()

const {
  authors,
  getAuthors,
} = useAuthor()

const {
  genres,
  getGenres,
} = useGenre()

const {
  languages,
  getLanguages,
} = useLanguage()

const {
  publishings,
  getPublishings,
} = usePublishing()

const {
  literatureGroups,
  getLiteratureGroups,
} = useLiteratureGroup()

const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const selectedBook = ref(null)
const toDelete = ref(null)

const loadDictionaries = async () => {
    await Promise.all([
        getAuthors(),
        getGenres(),
        getLanguages(),
        getPublishings(),
        getLiteratureGroups()
    ])
}

const handleRowClick = (row, column, event) => {
    router.push(`/admin/copies/${row.id}`)
}

const openCreateDialog = () => {
    isEdit.value = false
    selectedBook.value = null
    dialogVisible.value = true
}

const openEditDialog = (row) => {
    isEdit.value = true
    selectedBook.value = { ...row }
    dialogVisible.value = true
}

const handleDialogSave = async (payload) => {
    if (isEdit.value) {
        await updateBook(payload.id, payload.data)
        ElMessage.success('Книга обновлена')
    } else {
        await createBook(payload.data)
        ElMessage.success('Книга создана')
    }
    
    dialogVisible.value = false
    await getBooks()
}

const confirmDelete = (row) => {
    toDelete.value = row
    deleteDialogVisible.value = true
}

const deleteItem = async () => {
    await deleteBook(toDelete.value.id)
    ElMessage.success('Книга удалена')
    deleteDialogVisible.value = false
}

onMounted(() => {
    loadDictionaries()
    getBooks()
})
</script>

<style scoped>
.books-list {
    padding: 24px;
}

.book-link {
    color: #409eff;
    text-decoration: none;
}

.book-link:hover {
    text-decoration: underline;
}
</style>