<template>
    <el-tag :type="tagType" size="small">
        {{ label }}
    </el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    status: {
        type: String,
        required: true
    }
})

const statusMap = {
    ADDED: { label: 'Добавлен', type: 'info' },
    AVAILABLE: { label: 'Доступен', type: 'success' },
    IN_TRANSIT: { label: 'В обработке', type: 'warning' },
    RESERVED: { label: 'Забронирован', type: 'primary' },
    ISSUED: { label: 'Выдан', type: 'danger' },
    WRITTEN_OFF: { label: 'Списан', type: 'info' }
}

const label = computed(() => statusMap[props.status]?.label || props.status)
const tagType = computed(() => statusMap[props.status]?.type || 'info')
</script>