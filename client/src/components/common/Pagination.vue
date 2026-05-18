<template>
    <div class="pagination">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
    </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    page: { type: Number, default: 0 },
    size: { type: Number, default: 20 },
    total: { type: Number, default: 0 }
})

const emit = defineEmits(['update:page', 'update:size', 'change'])

const currentPage = computed({
    get: () => props.page + 1,
    set: (val) => emit('update:page', val - 1)
})

const pageSize = computed({
    get: () => props.size,
    set: (val) => emit('update:size', val)
})

const handleSizeChange = () => {
    emit('change')
}

const handleCurrentChange = () => {
    emit('change')
}
</script>

<style scoped>
.pagination {
    display: flex;
    justify-content: center;
    margin-top: 24px;
}
</style>