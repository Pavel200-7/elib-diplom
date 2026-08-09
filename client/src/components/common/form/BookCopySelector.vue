<template>
    <el-dialog
        v-model="dialogVisible"
        title="Выбор экземпляра книги"
        width="90%"
        :close-on-click-modal="false"
        @close="handleClose"
    >
        <!-- Шаг 1: Выбор книги -->
        <div v-if="step === 'book'">
            <BookFilter 
                :authors="authors"
                :genres="genres"
                @set-filter="(filter) => setFilters(filter)"
                @reset-filter="resetFilters()"
                @load="loadBooks()"
            />

            <BookTable 
                :books="books"
                :loading="loading"
                :row-clickable="false"
            >
                <template #actions="{ row }">
                    <el-button 
                        type="primary" 
                        size="small"
                        @click="selectBook(row)"
                    >
                        Выбрать книгу
                    </el-button>
                </template>
            </BookTable>

            <Pagination
                v-if="bookTotal > 0"
                :page="bookPage"
                :size="bookSize"
                :total="bookTotal"
                @update:page="bookPage = $event; loadBooks()"
                @update:size="bookSize = $event; loadBooks()"
            />
        </div>

        <!-- Шаг 2: Выбор экземпляра -->
        <div v-else-if="step === 'copy'">
            <div class="step-header">
                <el-button 
                    type="primary" 
                    link 
                    @click="step = 'book'"
                    class="back-button"
                >
                    <el-icon><ArrowLeft /></el-icon>
                    Вернуться к выбору книги
                </el-button>
                <div class="selected-book-info">
                    <span class="label">Выбрана книга:</span>
                    <span class="value">{{ selectedBook?.name }}</span>
                    <span class="author">{{ selectedBook?.authorName }}</span>
                </div>
            </div>

            <CopyFilter 
                :book-id="selectedBook?.id"
                :holders="holders"
                :statuses="copyStatuses"
                @set-filter="(filter) => setCopyFilters(filter)"
                @reset-filter="resetCopyFilters()"
                @load="loadCopies()"
            />

            <CopyTable 
                :items="copies"
                :loading="copyLoading"
                :statuses="copyStatuses"
                @handle-selection-change="() => {}"
            >
                <template #actions="{ row }">
                    <el-button 
                        type="primary" 
                        size="small"
                        :disabled="row.status !== 'AVAILABLE'"
                        @click="selectCopy(row)"
                    >
                        Выбрать копию
                    </el-button>
                </template>
            </CopyTable>

            <Pagination
                v-if="copyTotal > 0"
                :page="copyPage"
                :size="copySize"
                :total="copyTotal"
                @update:page="copyPage = $event; loadCopies()"
                @update:size="copySize = $event; loadCopies()"
            />
        </div>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleClose">Отмена</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'

import BookFilter from '@/components/books/BookFilter.vue'
import BookTable from '@/components/books/BookTable.vue'
import CopyFilter from '@/components/copies/CopyFilter.vue'
import CopyTable from '@/components/copies/CopyTable.vue'
import Pagination from '@/components/common/Pagination.vue'

import { useBook } from '@/services/composables/useBook'
import { useCopy } from '@/services/composables/useCopy'
import { useAuthor } from '@/services/composables/useAuthor'
import { useGenre } from '@/services/composables/useGenre'
import { useHolder } from '@/services/composables/useHolder'

const props = defineProps({
    visible: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['update:visible', 'select'])

const dialogVisible = ref(false)
const step = ref('book') // 'book' | 'copy'
const selectedBook = ref(null)
const selectedCopy = ref(null)

// Книги
const {
    books,
    page: bookPage,
    size: bookSize,
    total: bookTotal,
    loading: bookLoading,
    getBooks,
    setFilters,
    resetFilters
} = useBook()

// Копии
const {
    copies,
    page: copyPage,
    size: copySize,
    total: copyTotal,
    loading: copyLoading,
    statuses: copyStatuses,
    getCopies,
    setFilters: setCopyFilters,
    resetFilters: resetCopyFilters
} = useCopy()

// Справочники
const {
    authors,
    getAuthors
} = useAuthor()

const {
    genres,
    getGenres
} = useGenre()

const {
    holders,
    getHolders
} = useHolder()

const loading = computed(() => bookLoading.value || copyLoading.value)

// Загрузка справочников
const loadDictionaries = async () => {
    try {
        await Promise.all([
            getAuthors(),
            getGenres(),
            getHolders()
        ])
    } catch (error) {
        ElMessage.error('Ошибка загрузки справочников')
    }
}

// Загрузка книг
const loadBooks = async () => {
    await getBooks()
}

// Загрузка копий
const loadCopies = async () => {
    if (selectedBook.value) {
        setCopyFilters({ bookId: selectedBook.value.id })
        await getCopies()
    }
}

// Выбор книги
const selectBook = (book) => {
    selectedBook.value = book
    selectedCopy.value = null
    step.value = 'copy'
    // Сбрасываем фильтры копий
    resetCopyFilters()
    loadCopies()
}

// ✅ Выбор копии - сразу emits и закрывает форму
const selectCopy = (copy) => {
    selectedCopy.value = copy
    // Сразу отправляем ID копии
    emit('select', copy.id)
    // Закрываем форму
    handleClose()
}

// Обработка закрытия
const handleClose = () => {
    dialogVisible.value = false
    step.value = 'book'
    selectedBook.value = null
    selectedCopy.value = null
    resetFilters()
    resetCopyFilters()
    emit('update:visible', false)
}

// Сброс состояния при открытии
const resetState = () => {
    step.value = 'book'
    selectedBook.value = null
    selectedCopy.value = null
    resetFilters()
    resetCopyFilters()
}

// Следим за visible
watch(() => props.visible, (newVal) => {
    dialogVisible.value = newVal
    if (newVal) {
        resetState()
        loadBooks()
    }
})

// Следим за dialogVisible
watch(dialogVisible, (newVal) => {
    if (!newVal) {
        handleClose()
    }
})

// Инициализация
loadDictionaries()
</script>

<style scoped>
.step-header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
    padding: 16px;
    background: #f5f7fa;
    border-radius: 8px;
}

.back-button {
    flex-shrink: 0;
}

.selected-book-info {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
}

.selected-book-info .label {
    color: #909399;
    font-size: 14px;
}

.selected-book-info .value {
    color: #303133;
    font-weight: 600;
    font-size: 14px;
}

.selected-book-info .author {
    color: #606266;
    font-size: 13px;
    padding-left: 8px;
    border-left: 1px solid #dcdfe6;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}

:deep(.el-dialog) {
    max-height: 90vh;
    display: flex;
    flex-direction: column;
}

:deep(.el-dialog .el-dialog__body) {
    flex: 1;
    overflow-y: auto;
    max-height: 70vh;
}

:deep(.el-table) {
    font-size: 14px;
}
</style>