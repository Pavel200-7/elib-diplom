<template>
    <el-dialog
        :model-value="modelValue"
        :title="title"
        width="400px"
        @update:model-value="$emit('update:modelValue', $event)"
    >
        <p>{{ message }}</p>
        <template #footer>
            <el-button @click="$emit('update:modelValue', false)">Отмена</el-button>
            <el-button type="danger" @click="$emit('confirm')">Удалить</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
defineProps({
    modelValue: Boolean,
    title: String,
    message: String
})

defineEmits(['update:modelValue', 'confirm'])
</script>