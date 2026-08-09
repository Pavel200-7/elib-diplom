<template>
    <el-form 
        :model="form" 
        :rules="rules" 
        ref="formRef" 
        label-width="140px"
        class="edit-form"
    >
        <el-form-item label="Фамилия" prop="lastName">
            <el-input v-model="form.lastName" placeholder="Иванов" />
        </el-form-item>
        <el-form-item label="Имя" prop="firstName">
            <el-input v-model="form.firstName" placeholder="Иван" />
        </el-form-item>
        <el-form-item label="Отчество" prop="patronymic">
            <el-input v-model="form.patronymic" placeholder="Иванович" />
        </el-form-item>
        <el-form-item label="Дата рождения" prop="birthDate">
            <el-date-picker 
                v-model="form.birthDate" 
                type="date" 
                placeholder="Выберите дату"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
            />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="handleSave" :loading="saving">
                Сохранить
            </el-button>
            <el-button @click="handleCancel">Отмена</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'

const props = defineProps(['user'])
const emit = defineEmits(['save', 'cancel'])

const formRef = ref(null)
const saving = ref(false)

const form = reactive({
    id: null,
    firstName: '',
    lastName: '',
    patronymic: '',
    birthDate: null
})

const rules = {
    lastName: [{ required: true, message: 'Введите фамилию', trigger: 'blur' }],
    firstName: [{ required: true, message: 'Введите имя', trigger: 'blur' }],
    birthDate: [{ required: true, message: 'Выберите дату рождения', trigger: 'change' }]
}

const resetForm = () => {
    if (props.user) {
        form.id = props.user.id
        form.firstName = props.user.firstName || ''
        form.lastName = props.user.lastName || ''
        form.patronymic = props.user.patronymic || ''
        form.birthDate = props.user.birthDate || null
    }
    formRef.value?.clearValidate()
}

const handleSave = async () => {
    await formRef.value?.validate()
    saving.value = true
    try {
        emit('save', { ...form })
    } finally {
        saving.value = false
    }
}

const handleCancel = () => {
    emit('cancel')
}

// Заполняем форму при изменении user
watch(() => props.user, () => {
    resetForm()
}, { immediate: true })

// Экспортируем метод для родителя
defineExpose({
    resetForm
})
</script>

<style scoped>
.edit-form {
    margin-top: 10px;
}
</style>