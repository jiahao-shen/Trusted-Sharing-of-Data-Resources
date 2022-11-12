import App from './App.vue'
import router from './router'
import './assets/main.css'
import 'virtual:windi.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPersist from 'pinia-plugin-persist'
import '@purge-icons/generated'
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import JsonViewer from 'vue-json-viewer'

// 自定义组件
import { AllowButton } from '@/components/AllowButton'
import { ApplyStatusText } from '@/components/ApplyStatusText'
import { CopyText } from '@/components/CopyText'
import { OrganizationLogo } from '@/components/OrganizationLogo'
import { RejectButton } from '@/components/RejectButton'
import { UserLogo } from '@/components/UserLogo'
import { InvokeResponseDialog } from '@/components/InvokeResponseDialog'

const app = createApp(App)

const pinia = createPinia()
pinia.use(piniaPersist)
app.use(pinia)
app.use(router)
app.use(ElementPlus, {
	locale: zhCn,
	size: 'large',
})
app.use(JsonViewer)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component)
}

app.component('AllowButton', AllowButton)
app.component('ApplyStatusText', ApplyStatusText)
app.component('CopyText', CopyText)
app.component('OrganizationLogo', OrganizationLogo)
app.component('RejectButton', RejectButton)
app.component('UserLogo', UserLogo)
app.component('InvokeResponseDialog', InvokeResponseDialog)

app.mount('#app')
