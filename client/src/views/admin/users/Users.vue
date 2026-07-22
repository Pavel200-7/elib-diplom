<template>
    <div class="users-list">
        <UserHeader />

    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import UserHeader from './components/UserHeader.vue'

</script>

<style scoped>
.users-list {
    padding: 24px;
}
</style>