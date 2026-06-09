<template>
    <div class="advanced-filters">
        <el-row :gutter="20">
            <el-col :span="8">
                <el-form-item label="Автор">
                    <el-select v-model="localFilters.authorId" clearable filterable placeholder="Выберите автора">
                        <el-option
                            v-for="author in authors"
                            :key="author.id"
                            :label="author.name"
                            :value="author.id"
                        />
                    </el-select>
                </el-form-item>
            </el-col>
            
            <el-col :span="8">
                <el-form-item label="Жанр">
                    <el-select v-model="localFilters.genreId" clearable filterable placeholder="Выберите жанр">
                        <el-option
                            v-for="genre in genres"
                            :key="genre.id"
                            :label="genre.name"
                            :value="genre.id"
                        />
                    </el-select>
                </el-form-item>
            </el-col>
            
            <el-col :span="8">
                <el-form-item label="Издательство">
                    <el-select v-model="localFilters.publishingId" clearable filterable placeholder="Выберите издательство">
                        <el-option
                            v-for="pub in publishings"
                            :key="pub.id"
                            :label="pub.name"
                            :value="pub.id"
                        />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>

        <el-row :gutter="20">
            <el-col :span="8">
                <el-form-item label="Язык">
                    <el-select v-model="localFilters.languageId" clearable filterable placeholder="Выберите язык">
                        <el-option
                            v-for="lang in languages"
                            :key="lang.id"
                            :label="lang.name"
                            :value="lang.id"
                        />
                    </el-select>
                </el-form-item>
            </el-col>
            
            <el-col :span="8">
                <el-form-item label="Группа литературы">
                    <el-select v-model="localFilters.literatureGroupId" clearable filterable placeholder="Выберите группу">
                        <el-option
                            v-for="group in literatureGroups"
                            :key="group.id"
                            :label="group.name"
                            :value="group.id"
                        />
                    </el-select>
                </el-form-item>
            </el-col>
            
            <el-col :span="8">
                <el-form-item label="Возрастное ограничение">
                    <el-select v-model="localFilters.ageRestrictions" clearable placeholder="Выберите ограничение">
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
                <el-form-item label="Количество страниц">
                    <el-slider
                        v-model="pageRange"
                        range
                        :min="0"
                        :max="2000"
                        :step="50"
                    />
                    <div class="range-values">
                        <span>от {{ pageRange[0] }}</span>
                        <span>до {{ pageRange[1] }}</span>
                    </div>
                </el-form-item>
            </el-col>
            
            <el-col :span="12">
                <el-form-item label="Год издания">
                    <el-slider
                        v-model="yearRange"
                        range
                        :min="1450"
                        :max="2026"
                        :step="10"
                    />
                    <div class="range-values">
                        <span>от {{ yearRange[0] }}</span>
                        <span>до {{ yearRange[1] }}</span>
                    </div>
                </el-form-item>
            </el-col>
        </el-row>

        <div class="filters-actions">
            <el-button @click="resetFilters">Сбросить</el-button>
            <el-button type="primary" @click="applyFilters">Применить</el-button>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAll as getAllAuthors } from '@/services/api/authors'
import { getAll as getAllGenres } from '@/services/api/dictionaries'
import { getAll as getAllPublishings } from '@/services/api/publishings'
import { getAll as getAllLanguages } from '@/services/api/dictionaries'
import { getAll as getAllLiteratureGroups } from '@/services/api/dictionaries'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['apply'])

const localFilters = reactive({
    authorId: null,
    genreId: null,
    literatureGroupId: null,
    publishingId: null,
    languageId: null,
    ageRestrictions: null,
    pagesMin: null,
    pagesMax: null,
    publicationYearMin: null,
    publicationYearMax: null
})

const pageRange = ref([0, 2000])
const yearRange = ref([1450, 2026])

// Данные для селектов
const authors = ref([])
const genres = ref([])
const publishings = ref([])
const languages = ref([])
const literatureGroups = ref([])

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

const applyFilters = () => {
    const filters = {
        ...localFilters,
        pagesMin: pageRange.value[0] > 0 ? pageRange.value[0] : null,
        pagesMax: pageRange.value[1] < 2000 ? pageRange.value[1] : null,
        publicationYearMin: yearRange.value[0] > 1450 ? yearRange.value[0] : null,
        publicationYearMax: yearRange.value[1] < 2026 ? yearRange.value[1] : null
    }
    emit('apply', filters)
}

const resetFilters = () => {
    Object.keys(localFilters).forEach(key => {
        localFilters[key] = null
    })
    pageRange.value = [0, 2000]
    yearRange.value = [1450, 2026]
    applyFilters()
}

onMounted(() => {
    loadDictionaries()
})
</script>

<style scoped>
.advanced-filters {
    background: #f5f7fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
}

.range-values {
    display: flex;
    justify-content: space-between;
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
}

.filters-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 16px;
}
</style>