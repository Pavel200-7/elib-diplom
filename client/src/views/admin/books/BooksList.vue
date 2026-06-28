<template>
    <div class="books-list">
        <BookHeader 
            title="Книги"
            add-button-text="Добавить книгу"
            @add="openCreateDialog"
        />

        <BookFilter 
            :authors="authors"
            :genres="genres"
            @set-filter="(filter) => setFilters(filter)"
            @reset-filter="resetFilters()"
            @load="getBooks()"
        />

        <el-table :data="books" v-loading="loading" stripe>
            <el-table-column prop="name" label="Название" min-width="250" />
            <el-table-column prop="authorName" label="Автор" width="200" />
            <el-table-column prop="genreName" label="Жанр" width="150" />
            <el-table-column prop="publicationYear" label="Год" width="80" />
            <el-table-column label="Действия" width="180" fixed="right">
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

import BookHeader from './components/BookHeader.vue'
import BookFilter from './components/BookFilter.vue'
import Pagination from '@/components/common/Pagination.vue'
import BookDialog from './components/BookDialog.vue'
import BookDeleteConfirmDialog from './components/BookDeleteConfirmDialog.vue'

import { Edit, Delete } from '@element-plus/icons-vue'

import { useAuthor } from '@/services/composables/useAuthor'
import { useGenre } from '@/services/composables/useGenre'
import { usePublishing } from '@/services/composables/usePublishing'
import { useLanguage } from '@/services/composables/useLanguage'
import { useLiteratureGroup } from '@/services/composables/useLiteratureGroup.js'
import { useBook } from '@/services/composables/useBook'

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
    } else {
        await createBook(payload.data)
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
</style>