<template>
    <el-dialog :title="dialogTitle" v-model="visible" width="700px" @close="handleClose">
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
            <el-button @click="handleClose">Отмена</el-button>
            <el-button type="primary" @click="handleSave" :loading="saving">Сохранить</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
    visible: {
        type: Boolean,
        required: true
    },
    isEdit: {
        type: Boolean,
        default: false
    },
    initialData: {
        type: Object,
        default: null
    },
    authors: {
        type: Array,
        default: () => []
    },
    genres: {
        type: Array,
        default: () => []
    },
    publishings: {
        type: Array,
        default: () => []
    },
    languages: {
        type: Array,
        default: () => []
    },
    literatureGroups: {
        type: Array,
        default: () => []
    }
})

const emit = defineEmits(['update:visible', 'save', 'close'])

const formRef = ref()
const saving = ref(false)
const form = ref(getDefaultForm())

const rules = {
    name: [{ required: true, message: 'Введите название', trigger: 'blur' }],
    authorId: [{ required: true, message: 'Выберите автора', trigger: 'change' }],
    genreId: [{ required: true, message: 'Выберите жанр', trigger: 'change' }],
    publishingId: [{ required: true, message: 'Выберите издательство', trigger: 'change' }],
    languageId: [{ required: true, message: 'Выберите язык', trigger: 'change' }]
}

const dialogTitle = computed(() => props.isEdit ? 'Редактировать книгу' : 'Добавить книгу')

const visible = computed({
    get: () => props.visible,
    set: (val) => emit('update:visible', val)
})

function getDefaultForm() {
    return {
        id: null,
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
}

function resetForm() {
    form.value = getDefaultForm()
    nextTick(() => {
        formRef.value?.clearValidate()
    })
}

function setFormData(data) {
    if (data) {
        form.value = {
            id: data.id || null,
            name: data.name || '',
            authorId: data.authorId || null,
            genreId: data.genreId || null,
            literatureGroupId: data.literatureGroupId || null,
            publishingId: data.publishingId || null,
            languageId: data.languageId || null,
            pages: data.pages || null,
            publicationYear: data.publicationYear || null,
            ageRestrictions: data.ageRestrictions || null,
            description: data.description || ''
        }
    } else {
        resetForm()
    }
}

watch(() => props.visible, (newVal) => {
    if (newVal) {
        setFormData(props.initialData)
    } else {
        resetForm()
    }
})

const handleSave = async () => {
    try {
        await formRef.value?.validate()
        saving.value = true
        
        const data = { ...form.value }
        
        if (props.isEdit) {
            emit('save', { id: data.id, data })
        } else {
            emit('save', { data })
        }
    } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Ошибка сохранения')
    } finally {
        saving.value = false
    }
}

const handleClose = () => {
    visible.value = false
    emit('close')
    resetForm()
}

defineExpose({
    setFormData,
    resetForm
})
</script>

<style scoped>

</style>