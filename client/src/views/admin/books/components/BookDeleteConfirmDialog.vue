<template>
    <ConfirmDialog
        v-model="visible"
        :title="title"
        :message="message"
        @confirm="handleConfirm"
    />
</template>

<script setup>
import { computed } from 'vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true
    },
    itemName: {
        type: String,
        default: ''
    },
    itemType: {
        type: String,
        default: 'запись'
    }
})

const emit = defineEmits(['update:modelValue', 'confirm'])

const visible = computed({
    get: () => props.modelValue,
    set: (val) => emit('update:modelValue', val)
})

const title = computed(() => `Удалить ${props.itemType}`)
const message = computed(() => {
    if (props.itemName) {
        return `Вы уверены, что хотите удалить ${props.itemType} "${props.itemName}"? Это действие нельзя отменить.`
    }
    return `Вы уверены, что хотите удалить ${props.itemType}? Это действие нельзя отменить.`
})

const handleConfirm = () => {
    emit('confirm')
}
</script>