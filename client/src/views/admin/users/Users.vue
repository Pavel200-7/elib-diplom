<template>
    <div class="users-list">
        <UserHeader />

        <UserFilter 
            :statuses="statuses"
            @set-filter="(filter) => setFilters(filter)"
            @reset-filter="resetFilters()"
            @load="handleLoad()"
        />

        <UserTable 
            :items="users"
            :loading="loading"
            :statuses="statuses"
            @activate-user="id => handleActivateUser(id)"
            @open-details="row => handleOpenDetails(row)"
        />

        <Pagination
            v-if="total > 0"
            :page="page"
            :size="size"
            :total="total"
            @update:page="page = $event; getUsers()"
            @update:size="size = $event; getUsers()"
        />
    </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

import UserHeader from '@/components/users/users/UserHeader.vue'
import UserFilter from '@/components/users/users/UserFilter.vue'
import UserTable from '@/components/users/users/UserTable.vue'
import Pagination from '@/components/common/Pagination.vue'

import { useUser } from '@/services/composables/useUser'

const router = useRouter()

const {
    users,
    page,
    size,
    total,
    loading,
    statuses,
    getUsers,
    activateUser,
    setFilters,
    resetFilters,
} = useUser()

const handleLoad = async () => {
    await getUsers()
}

const handleActivateUser = async (id) => {
    await activateUser(id)
    ElMessage.success('Пользователь активирован')
}

const handleOpenDetails = (row) => {
    router.push({
        name: 'AdminUsersDetails',
        params: { id: row.id }
    })
}

onMounted(async () => {
    await getUsers()
})
</script>

<style scoped>
.users-list {
    padding: 24px;
}
</style>