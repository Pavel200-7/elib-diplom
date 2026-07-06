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
    },
    statuses: {
        type: Array,
        required: true
    }
})

const statusMap = {
    ADDED: { type: 'info' },
    AVAILABLE: { type: 'success' },
    IN_TRANSIT: { type: 'warning' },
    RESERVED: { type: 'primary' },
    ISSUED: { type: 'danger' },
    WRITTEN_OFF: { type: 'info' }
}

const label = computed(() => props.statuses.find(s => s.value == props.status).label || '')
const tagType = computed(() => statusMap[props.status]?.type || 'info')
</script>