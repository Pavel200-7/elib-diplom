<template>
    <div class="char-row">
        <span class="char-label">{{ label }}</span>
        <span class="char-dots"></span>
        <span class="char-value">{{ value || '—' }}</span>
    </div>
</template>

<script setup>
defineProps({
    label: {
        type: String,
        required: true
    },
    value: {
        type: [String, Number],
        default: null
    }
})
</script>

<style scoped>
.char-row {
    display: flex;
    align-items: baseline;
    padding: 4px 0;
    font-size: 14px;
    line-height: 1.6;
}

.char-label {
    color: #909399;
    white-space: nowrap;
    flex-shrink: 0;
}

.char-dots {
    flex: 1;
    min-width: 20px;
    border-bottom: 1px dotted #d0d0d0;
    margin: 0 8px;
    height: 1px;
}

.char-value {
    color: #2c3e50;
    font-weight: 500;
    white-space: nowrap;
    flex-shrink: 0;
    max-width: 300px;
    overflow: hidden;
    text-overflow: ellipsis;
}

/* Адаптивность для мобильных устройств */
@media (max-width: 480px) {
    .char-row {
        font-size: 12px;
        flex-wrap: wrap;
    }

    .char-dots {
        display: none;
    }

    .char-label {
        width: 100%;
        color: #606266;
        font-weight: 600;
    }

    .char-value {
        width: 100%;
        white-space: normal;
        max-width: 100%;
        padding-left: 0;
        margin-top: -2px;
    }
}
</style>