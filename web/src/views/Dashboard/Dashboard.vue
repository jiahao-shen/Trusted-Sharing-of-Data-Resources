<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useFullscreen } from '@vueuse/core'
import { Icon } from '@/components/Icon'

const route = useRoute()
const router = useRouter()
const breadcrumbList = ref(Array())
const { toggle, isFullscreen } = useFullscreen()

const getBreadcrumb = (matched: Array<any>) => {
	breadcrumbList.value = matched
}

const user = {
	name: '沈嘉浩',
	imgURL: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
}

const collapse = ref(false)
const toggleCollapse = () => {
	collapse.value = !collapse.value
}

getBreadcrumb(route.matched)

watch(
	() => route.path,
	(value: String) => {
		getBreadcrumb(route.matched)
	}
)

const logout = () => {
	router.push('/login')
}
</script>

<template>
	<div class="w-full h-100vh flex">
		<div :class="[`${collapse ? 'w-auto' : 'w-300px'}`, 'h-full flex flex-col']">
			<div class="w-full !h-60px p-5px flex items-center border-b-1 border-[var(--el-border-color)]">
				<el-image class="h-40px ml-5px" src="/src/assets/img/logo.svg" />
				<h1 class="text-2xl mx-10px" v-if="!collapse">Hawk Eye</h1>
			</div>
			<el-menu class="flex-grow" :collapse="collapse" router>
				<el-sub-menu index="control">
					<template #title>
						<el-icon><Setting /></el-icon>
						<span class="text-base">机构相关</span>
					</template>
					<el-menu-item index="control-personnel-management">人员管理</el-menu-item>
					<el-menu-item index="control-approval-process">审批流程</el-menu-item>
					<el-menu-item index="control-key-management">秘钥管理</el-menu-item>
					<el-menu-item index="control-release-deployment">发布部署</el-menu-item>
				</el-sub-menu>
				<el-sub-menu index="api">
					<template #title>
						<el-icon><Collection /></el-icon>
						<span class="text-base">API管理</span>
					</template>
					<el-menu-item index="/dashboard/api/registration">API注册</el-menu-item>
					<el-menu-item index="/dashboard/api/request">API申请</el-menu-item>
					<el-menu-item index="/dashboard/api/authorization">API授权</el-menu-item>
					<el-menu-item index="/dashboard/api/information">我的API</el-menu-item>
				</el-sub-menu>
				<el-sub-menu index="message">
					<template #title>
						<el-icon><Message /></el-icon>
						<span class="text-base">消息协同</span>
					</template>
					<el-menu-item index="topic-registration">Topic注册</el-menu-item>
					<el-menu-item index="topic-request">Topic申请</el-menu-item>
					<el-menu-item index="topic-information">我的Topic</el-menu-item>
				</el-sub-menu>
				<el-sub-menu index="statistics">
					<template #title>
						<el-icon><PieChart /></el-icon>
						<span class="text-base">统计分析</span>
					</template>
					<el-menu-item index="statistics">统计分析</el-menu-item>
				</el-sub-menu>
			</el-menu>
		</div>
		<div class="w-full h-full flex flex-col">
			<div class="w-full h-60px border-b-1 border-[var(--el-border-color)] flex justify-between">
				<div class="h-full flex justify-center items-center">
					<Icon
						class="h-full hover:bg-gray-100 px-10px"
						:size="30"
						icon="ant-design:menu-fold-outlined"
						color="#888888"
						@click="toggleCollapse"
					/>

					<el-breadcrumb class="mx-10px my-auto" separator="/">
						<el-breadcrumb-item v-for="item in breadcrumbList" :to="item.path">
							<span class="text-base">{{ item.name }}</span></el-breadcrumb-item
						>
					</el-breadcrumb>
				</div>

				<div class="h-full flex justify-center items-center">
					<Icon
						class="h-full hover:bg-gray-100 px-10px"
						:size="30"
						:icon="isFullscreen ? 'zmdi:fullscreen-exit' : 'zmdi:fullscreen'"
						color="#888888"
						@click="toggle"
					/>
					<el-button class="mx-5px my-auto" size="large" icon="Search" round>搜索</el-button>
					<el-avatar size="default" :src="user.imgURL" style="margin: auto 5px" />
					<el-menu mode="horizontal" :ellipsis="false">
						<el-sub-menu index="user">
							<template #title>{{ user.name }}</template>
							<el-menu-item index="user-info">个人信息</el-menu-item>
							<el-menu-item index="user-logout" @click="logout">退出登录</el-menu-item>
						</el-sub-menu>
					</el-menu>
				</div>
			</div>
			<div class="w-full flex-1 overflow-y-auto overflow-x-hidden">
				<router-view />
			</div>

			<div class="w-full h-30px flex justify-center items-center">
				<h3>Copyright ©2022-北京航空航天大学</h3>
			</div>
		</div>
	</div>
</template>

<style scoped>
.el-menu--horizontal {
	border-bottom: none;
}
</style>
