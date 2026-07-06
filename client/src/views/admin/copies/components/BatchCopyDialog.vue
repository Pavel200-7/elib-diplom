<template>
    <el-dialog title="Массовое создание экземпляров" v-model="dialogVisible" width="600px">
        <el-form-item label="Книга" prop="bookId">
                <el-input 
                    v-model="bookId"
                    :value="book?.name || 'Не выбрана'" 
                    disabled
                    placeholder="Книга не выбрана"
                />
            </el-form-item>
        <el-alert 
            title="Введите данные ISBN в формате: ISBN-10 (XXXXXXXXXX) или ISBN-13 (978XXXXXXXXXX, 979XXXXXXXXXX)"
            type="info"
            :closable="false"
            style="margin-bottom: 16px"
        />
        <el-input
            v-model="batchData"
            type="textarea"
            :rows="10"
            placeholder="9783161484100&#10;9783161484100&#10;9783161484100"
        />
        <template #footer>
            <el-button type="primary" @click="batchCreate">
                Создать
            </el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'


const emit = defineEmits(['save', 'update:visible'])
const props = defineProps(['visible', 'book'])

const batchData = ref('')
const bookId = ref(null) 

const dialogVisible = computed({
    get: () => props.visible,
    set: (value) => {
        emit('update:visible', value)
    } 
})

const batchCreate = async () => {
    const lines = batchData.value.split('\n').filter(l => l.trim())
    const copiesList = lines.map(line => {
        const isbn = line
        return { 
            isbn: isbn, 
            bookId: bookId.value }
    })
    emit('save', copiesList)
}

watch(() => props.visible, (newVal) => {
    if (newVal) {
        nextTick(() => {
            batchData.value = ''
            bookId.value = props.book?.id || null
        })
    }
}, { immediate: true })
</script>