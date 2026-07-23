import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import { ElMessage } from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './assets/main.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(pinia)
app.use(router)
app.use(ElementPlus)

app.config.errorHandler = (err, vm, info) => {
    const message = err.response?.data?.message 
        || err.message 
        || 'Произошла непредвиденная ошибка. Просьба обратится в техническую поддержку.'
    ElMessage.error(message)
    
    console.log("Ошибка - проверь")
    console.error(err, info)
    console.log(message)
}


app.mount('#app')

