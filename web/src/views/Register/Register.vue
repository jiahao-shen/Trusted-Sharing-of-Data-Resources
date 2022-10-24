<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
</script>

<template>
	<div v-if="route.name === '注册'" class="w-full h-full flex justify-center items-center">
		<div class="w-full h-full flex text-gray-500">
			<div class="w-[50%] h-full flex flex-col justify-center items-center border-r-1 cursor-pointer hover:bg-gray-200" @click="router.push('/register/organization')">
				<el-icon :size="150"><OfficeBuilding /></el-icon>
				<h2 class="text-2xl">注册新机构</h2>
			</div>
			<div class="w-[50%] h-full flex flex-col justify-center items-center border-l-1 cursor-pointer hover:bg-gray-200" @click="router.push('/register/progress')">
				<el-icon :size="150"><Search /></el-icon>
				<h2 class="text-2xl">注册进度查询</h2>
			</div>
		</div>
	</div>
	<router-view v-else></router-view>
</template>

<style lang="less" scoped></style>
