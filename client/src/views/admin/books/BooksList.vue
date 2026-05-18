<template>
    <div class="books-list">
        <div class="header">
            <h2>Книги</h2>
            <el-button type="primary" @click="openCreateDialog">
                <el-icon><Plus /></el-icon>
                Добавить книгу
            </el-button>
        </div>

        <!-- Фильтры -->
        <div class="filters">
            <el-input
                v-model="filters.name"
                placeholder="Поиск по названию"
                style="width: 250px"
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
            <el-select v-model="filters.authorId" placeholder="Автор" clearable filterable style="width: 200px">
                <el-option v-for="a in authors" :key="a.id" :label="a.name" :value="a.id" />
            </el-select>
            <el-select v-model="filters.genreId" placeholder="Жанр" clearable style="width: 150px">
                <el-option v-for="g in genres" :key="g.id" :label="g.name" :value="g.id" />
            </el-select>
            <el-button type="primary" @click="loadItems">Применить</el-button>
            <el-button @click="resetFilters">Сбросить</el-button>
        </div>

        <el-table :data="items" v-loading="loading" stripe>
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
            @update:page="page = $event; loadItems()"
            @update:size="size = $event; loadItems()"
        />

        <!-- Диалог создания/редактирования -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
                <el-form-item label="Название" prop="name">
                    <el-input v-model="form.name" maxlength="500" show-word-limit />
                </el-form-item>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Автор" prop="authorId">
                            <el-select v-model="form.authorId" filterable placeholder="Выберите автора" style="width: 100%">
                                <el-option v-for="a in authors" :key="a.id" :label="a.name" :value="a.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Жанр" prop="genreId">
                            <el-select v-model="form.genreId" filterable placeholder="Выберите жанр" style="width: 100%">
                                <el-option v-for="g in genres" :key="g.id" :label="g.name" :value="g.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Издательство" prop="publishingId">
                            <el-select v-model="form.publishingId" filterable placeholder="Выберите издательство" style="width: 100%">
                                <el-option v-for="p in publishings" :key="p.id" :label="p.name" :value="p.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Язык" prop="languageId">
                            <el-select v-model="form.languageId" filterable placeholder="Выберите язык" style="width: 100%">
                                <el-option v-for="l in languages" :key="l.id" :label="l.name" :value="l.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Группа литературы" prop="literatureGroupId">
                            <el-select v-model="form.literatureGroupId" clearable placeholder="Выберите группу" style="width: 100%">
                                <el-option v-for="g in literatureGroups" :key="g.id" :label="g.name" :value="g.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Возрастное ограничение" prop="ageRestrictions">
                            <el-select v-model="form.ageRestrictions" clearable placeholder="Выберите ограничение" style="width: 100%">
                                <el-option label="0+" value="ZERO_PLUS" />
                                <el-option label="6+" value="SIX_PLUS" />
                                <el-option label="12+" value="TWELVE_PLUS" />
                                <el-option label="16+" value="SIXTEEN_PLUS" />
                                <el-option label="18+" value="EIGHTEEN_PLUS" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Год издания" prop="publicationYear">
                            <el-input-number v-model="form.publicationYear" :min="1450" :max="2026" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Количество страниц" prop="pages">
                            <el-input-number v-model="form.pages" :min="1" :max="10000" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="Описание" prop="description">
                    <el-input v-model="form.description" type="textarea" :rows="4" />
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
            :message="`Удалить книгу ${toDelete?.name}?`"
            @confirm="deleteItem"
        />
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Delete, Search } from '@element-plus/icons-vue'
import * as booksApi from '@/services/api/books'
import { getAll as getAllAuthors } from '@/services/api/authors'
import { getAll as getAllGenres } from '@/services/api/dictionaries'
import { getAll as getAllPublishings } from '@/services/api/publishings'
import { getAll as getAllLanguages } from '@/services/api/dictionaries'
import { getAll as getAllLiteratureGroups } from '@/services/api/dictionaries'
import Pagination from '@/components/common/Pagination.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const items = ref([])
const loading = ref(false)
const saving = ref(false)
const total = ref(0)
const page = ref(0)
const size = ref(20)
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({})
const toDelete = ref(null)
const formRef = ref()

// Справочники
const authors = ref([])
const genres = ref([])
const publishings = ref([])
const languages = ref([])
const literatureGroups = ref([])

// Фильтры
const filters = reactive({
    name: '',
    authorId: null,
    genreId: null
})

const rules = {
    name: [{ required: true, message: 'Введите название', trigger: 'blur' }],
    authorId: [{ required: true, message: 'Выберите автора', trigger: 'change' }],
    genreId: [{ required: true, message: 'Выберите жанр', trigger: 'change' }],
    publishingId: [{ required: true, message: 'Выберите издательство', trigger: 'change' }],
    languageId: [{ required: true, message: 'Выберите язык', trigger: 'change' }]
}

const dialogTitle = computed(() => isEdit.value ? 'Редактировать книгу' : 'Добавить книгу')

const loadDictionaries = async () => {
    try {
        const [authorsRes, genresRes, publishingsRes, languagesRes, groupsRes] = await Promise.all([
            getAllAuthors(),
            getAllGenres('/genres'),
            getAllPublishings(),
            getAllLanguages('/languages'),
            getAllLiteratureGroups('/literature-groups')
        ])
        authors.value = authorsRes.data
        genres.value = genresRes.data
        publishings.value = publishingsRes.data
        languages.value = languagesRes.data
        literatureGroups.value = groupsRes.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки справочников')
    }
}

const loadItems = async () => {
    loading.value = true
    try {
        const criteria = {
            searchCriteria: {
                name: filters.name || null,
                authorId: filters.authorId || null,
                genreId: filters.genreId || null
            },
            sortCriteria: {
                sortBy: 'NAME',
                sortDirection: 'ASC'
            },
            pageData: {
                page: page.value,
                size: size.value
            }
        }
        const response = await booksApi.getAll(criteria)
        items.value = response.data.content
        total.value = response.data.totalElements
    } catch (error) {
        ElMessage.error('Ошибка загрузки книг')
        console.error(error)
    } finally {
        loading.value = false
    }
}

const resetFilters = () => {
    filters.name = ''
    filters.authorId = null
    filters.genreId = null
    page.value = 0
    loadItems()
}

const openCreateDialog = () => {
    isEdit.value = false
    form.value = {
        name: '',
        authorId: null,
        genreId: null,
        literatureGroupId: null,
        publishingId: null,
        languageId: null,
        pages: null,
        publicationYear: null,
        ageRestrictions: null,
        description: ''
    }
    dialogVisible.value = true
}

const openEditDialog = (row) => {
    isEdit.value = true
    form.value = {
        id: row.id,
        name: row.name,
        authorId: row.authorId,
        genreId: row.genreId,
        literatureGroupId: row.literatureGroupId,
        publishingId: row.publishingId,
        languageId: row.languageId,
        pages: row.pages,
        publicationYear: row.publicationYear,
        ageRestrictions: row.ageRestrictions,
        description: row.description
    }
    dialogVisible.value = true
}

const save = async () => {
    try {
        await formRef.value?.validate()
        saving.value = true
        
        const data = { ...form.value }
        
        if (isEdit.value) {
            await booksApi.update(data.id, data)
            ElMessage.success('Книга обновлена')
        } else {
            await booksApi.create(data)
            ElMessage.success('Книга добавлена')
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
        await booksApi.deleteItem(toDelete.value.id)
        ElMessage.success('Книга удалена')
        deleteDialogVisible.value = false
        await loadItems()
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка удаления')
    }
}

onMounted(() => {
    loadDictionaries()
    loadItems()
})
</script>

<style scoped>
.books-list {
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
</style>