<template>
    <div class="default-layout">
        <NavBar />
        <main class="main-content">
            <router-view />
        </main>
    </div>
</template>

<script setup>
import NavBar from '@/components/common/NavBar.vue'
</script>

<style scoped>
.default-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.main-content {
    flex: 1;
    padding: 24px;
    max-width: 1400px;
    margin: 0 auto;
    width: 100%;
}
</style>