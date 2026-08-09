<template>
    <div class="book-detail" v-loading="loading">
        <template v-if="book">
            <div class="book-header">
                <div class="book-cover">
                    <el-icon :size="120"><Reading /></el-icon>
                </div>
                <div class="book-info">
                    <h1>{{ book.name }}</h1>
                    <div class="meta">
                        <el-tag size="large">{{ book.genreName }}</el-tag>
                        <span>{{ book.publicationYear }}</span>
                    </div>
                    
                    <!-- Характеристики с точками -->
                    <div class="characteristics">
                        <CharRow 
                            label="Автор" 
                            :value="book.authorName" 
                        />
                        <CharRow 
                            label="Год выпуска" 
                            :value="book.publicationYear" 
                        />
                        <CharRow 
                            label="Издательство" 
                            :value="book.publishingName" 
                        />
                        <CharRow 
                            label="Язык издания" 
                            :value="book.languageName" 
                        />
                        <CharRow 
                            label="Количество страниц" 
                            :value="book.pages" 
                        />
                        <CharRow 
                            label="Возрастное ограничение" 
                            :value="getAgeRestrictionLabel(book.ageRestrictions)" 
                        />
                    </div>

                    <!-- Блок действий -->
                    <div>
                        <span v-if="availableCount > 0" class="available">
                            {{ availableCount }} экз. доступно
                        </span>
                        <span v-else class="unavailable">
                            Нет в наличии
                        </span>
                    </div>
                </div>
            </div>
            
            <div class="book-description" v-if="book.description">
                <h3>Описание</h3>
                <p>{{ book.description }}</p>
            </div>
        </template>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Reading } from '@element-plus/icons-vue'
import CharRow from '@/components/common/row/CharRow.vue'

import { getById, getAvailableCount } from '@/services/api/books'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const book = ref(null)
const availableCount = ref(0)

const getAgeRestrictionLabel = (value) => {
    const map = {
        ZERO_PLUS: '0+',
        SIX_PLUS: '6+',
        TWELVE_PLUS: '12+',
        SIXTEEN_PLUS: '16+',
        EIGHTEEN_PLUS: '18+'
    }
    return map[value] || value
}

const loadBook = async () => {
    loading.value = true
    try {
        const bookId = route.params.id
        const [bookRes, countRes] = await Promise.all([
            getById(bookId),
            getAvailableCount(bookId)
        ])
        book.value = bookRes.data
        availableCount.value = countRes.data
    } catch (error) {
        ElMessage.error('Ошибка загрузки книги')
        router.push('/')
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    loadBook()
})
</script>

<style scoped>
.book-detail {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
}

.book-header {
    display: flex;
    gap: 40px;
    background: white;
    padding: 32px;
    border-radius: 16px;
    margin-bottom: 32px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.book-cover {
    width: 200px;
    height: 280px;
    background: #f5f7fa;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #c0c4cc;
    flex-shrink: 0;
}

.book-info {
    flex: 1;
    min-width: 0;
}

.book-info h1 {
    font-size: 28px;
    margin: 0 0 12px 0;
    color: #2c3e50;
}

.meta {
    display: flex;
    gap: 16px;
    align-items: center;
    margin-bottom: 20px;
}

.characteristics {
    margin: 20px 0;
    max-width: 500px;
}

.available {
    color: #67c23a;
    font-weight: 500;
}

.unavailable {
    color: #f56c6c;
    font-weight: 500;
}

.book-description {
    background: white;
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.book-description h3 {
    margin: 0 0 12px 0;
    font-size: 18px;
    color: #2c3e50;
}

.book-description p {
    line-height: 1.8;
    color: #606266;
    margin: 0;
}

/* Адаптивность */
@media (max-width: 768px) {
    .book-header {
        flex-direction: column;
        align-items: center;
        padding: 20px;
    }

    .book-cover {
        width: 150px;
        height: 200px;
    }

    .book-info h1 {
        font-size: 22px;
        text-align: center;
    }

    .meta {
        justify-content: center;
    }

    .characteristics {
        max-width: 100%;
        margin: 16px auto;
    }

    .actions {
        justify-content: center;
    }
}
</style>