<script setup lang="ts">
import pageError from '@/assets/img/404.svg'
import networkError from '@/assets/img/500.svg'
import noPermission from '@/assets/img/403.svg'
import { useRouter } from 'vue-router'

interface ErrorMap {
	url: string
	message: string
	buttonText: string
}

const errorMap: {
	[key: string]: ErrorMap
} = {
	'404': {
		url: pageError,
		message: '抱歉, 您访问的页面不存在',
		buttonText: '返回首页',
	},
	'500': {
		url: networkError,
		message: '抱歉, 服务器报告错误',
		buttonText: '返回首页',
	},
	'403': {
		url: noPermission,
		message: '抱歉, 您无权访问此页面',
		buttonText: '返回首页',
	},
}

const props = defineProps<{
	type: string
}>()

const router = useRouter()

const btnClick = () => {
	router.push('/')
}
</script>

<template>
	<div class="flex justify-center">
		<div class="text-center">
			<img width="350" :src="errorMap[type].url" alt="" />
			<div class="text-14px text-[var(--el-color-info)]">{{ errorMap[type].message }}</div>
			<div class="mt-20px">
				<el-button type="primary" @click="btnClick">{{ errorMap[type].buttonText }}</el-button>
			</div>
		</div>
	</div>
</template>
