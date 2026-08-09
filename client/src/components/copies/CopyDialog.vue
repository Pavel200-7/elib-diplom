<template> 
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
            <el-form-item label="Книга" prop="bookId">
                <el-input 
                    v-model="form.id"
                    :value="book?.name || 'Не выбрана'" 
                    disabled
                    placeholder="Книга не выбрана"
                />
            </el-form-item>
            <el-form-item label="ISBN" prop="isbn">
                <el-input v-model="form.isbn" placeholder="9783161484100" />
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button type="primary" @click="handleSave">Сохранить</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick  } from 'vue'

const emit = defineEmits(['save', 'update:visible'])
const props = defineProps(['visible', 'book', 'isEdit', 'data'])

const form = ref({})
const formRef = ref(null)


const rules = {
    bookId: [{ required: true, message: 'Выберите книгу', trigger: 'change' }],
    isbn: [{ required: true, message: 'Введите идентификатор книги (isbn)', trigger: 'blur' }]
}

const dialogTitle = computed(() => props.isEdit ? 'Редактировать экземпляр' : 'Добавить экземпляр')

const dialogVisible = computed({
    get: () => props.visible,
    set: (value) => {
        emit('update:visible', value)
    } 
})

const handleSave = async () => {
    form.value.bookId = props.book?.id
    emit('save', form.value)
}

function getDefaultForm() {
    return {
        isbn: '',
        bookId: props.book?.id || null,
    }
}

function getFilledForm() {
    const data = props.data
    console.log(data)
    return {
        isbn: data.isbn || '',
        bookId: props.book?.id || null,
    }
}

watch(() => props.visible, (newVal) => {
    if (newVal) {
        nextTick(() => {
            if (props.isEdit) {
                form.value = getFilledForm()
            } else {
                form.value = getDefaultForm()
            }
            formRef.value?.clearValidate()
        })
    }
}, { immediate: true })
</script>