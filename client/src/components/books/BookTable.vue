<template>
    <el-table 
        :data="books" 
        v-loading="loading" 
        stripe
        @row-click="handleRowClick"
        style="width: 100%"
    >
        <el-table-column prop="name" label="Название" min-width="250">
            <template #default="{ row }">
            </template>    
        </el-table-column>
        <el-table-column prop="authorName" label="Автор" width="200" />
        <el-table-column prop="genreName" label="Жанр" width="150" />
        <el-table-column prop="publicationYear" label="Год" width="80" />
        
        <!-- Слот для действий -->
        <el-table-column 
            v-if="$slots.actions" 
            label="Действия" 
            width="180" 
            fixed="right"
        >
            <template #default="scope">
                <slot name="actions" :row="scope.row" />
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
const props = defineProps({
    books: {
        type: Array,
        required: true
    },
    loading: {
        type: Boolean,
        default: false
    },
    rowClickable: {
        type: Boolean,
        default: true
    }
})

const emit = defineEmits(['rowClick'])

const handleRowClick = (row, column, event) => {
    if (props.rowClickable) {
        emit('rowClick', row, column, event)
    }
}
</script>

<style scoped>
.book-link {
    color: #409eff;
    text-decoration: none;
}

.book-link:hover {
    text-decoration: underline;
}

:deep(.el-table__row) {
    cursor: pointer;
}
</style>