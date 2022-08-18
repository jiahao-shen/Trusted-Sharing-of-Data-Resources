<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
import { ref, onMounted, onActivated, onBeforeUpdate } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router'

let user = {
	name: '沈嘉浩',
	imgURL: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
}
const sideMenuCollapsed = ref(false)
</script>

<template>
	<el-container class="container">
		<el-header class="header">
			<div style="display: flex; justify-content: space-between; width: 100%; height: 100%">
				<div style="display: flex">
					<el-image src="/src/assets/logo.svg" style="margin: 5px" @click="sideMenuCollapsed = !sideMenuCollapsed" />
					<h1 style="margin: auto 5px">数据资源可信共享平台</h1>
				</div>
				<div style="display: flex">
					<el-button size="large" icon="Search" style="margin: auto 5px" round>搜索</el-button>
					<el-avatar size="default" :src="user.imgURL" style="margin: auto 5px" />
					<el-menu mode="horizontal" :ellipsis="false">
						<el-sub-menu index="user">
							<template #title>{{ user.name }}</template>
							<el-menu-item index="user-info">个人信息</el-menu-item>
							<el-menu-item index="user-logout">退出登录</el-menu-item>
						</el-sub-menu>
					</el-menu>
				</div>
			</div>
		</el-header>

		<el-container class="container">
			<el-aside class="aside">
				<el-menu :collapse="sideMenuCollapsed" router>
					<el-sub-menu index="control">
						<template #title>
							<el-icon><Setting /></el-icon>
							<span>控制台</span>
						</template>
						<el-menu-item index="control-personnel-management">人员管理</el-menu-item>
						<el-menu-item index="control-approval-process">审批流程</el-menu-item>
						<el-menu-item index="control-key-management">秘钥管理</el-menu-item>
						<el-menu-item index="control-release-deployment">发布部署</el-menu-item>
					</el-sub-menu>
					<el-sub-menu index="api">
						<template #title>
							<el-icon><Collection /></el-icon>
							<span>API管理</span>
						</template>
						<el-menu-item index="/api/registration">API注册</el-menu-item>
						<el-menu-item index="/api/request">API申请</el-menu-item>
						<el-menu-item index="/api/authorization">API授权</el-menu-item>
						<el-menu-item index="/api/information">我的API</el-menu-item>
					</el-sub-menu>
					<el-sub-menu index="message">
						<template #title>
							<el-icon><Message /></el-icon>
							<span>消息协同</span>
						</template>
						<el-menu-item index="topic-registration">Topic注册</el-menu-item>
						<el-menu-item index="topic-request">Topic申请</el-menu-item>
						<el-menu-item index="topic-information">我的Topic</el-menu-item>
					</el-sub-menu>
					<el-sub-menu index="statistics">
						<template #title>
							<el-icon><PieChart /></el-icon>
							<span>统计分析</span>
						</template>
						<el-menu-item index="statistics">统计分析</el-menu-item>
					</el-sub-menu>
				</el-menu>
			</el-aside>

			<el-container>
				<el-main class="main">
					<!-- <el-breadcrumb :separator-icon="ArrowRight">
						<el-breadcrumb-item>{{ $route }}</el-breadcrumb-item>
					</el-breadcrumb> -->
					<RouterView />
				</el-main>
				<el-footer class="footer">
					<h4 style="margin: 5px">Copyright @ 2022 北京航空航天大学.</h4>
				</el-footer>
			</el-container>
		</el-container>
	</el-container>
</template>

<style scoped>
.main {
	padding: 0;
}
.footer {
	height: 40px;
	color: #aaaaaa;
	background-color: #dddddd;
	border-top: 1px solid var(--el-border-color);
	text-align: center;
}
.el-menu--horizontal {
	border-bottom: none;
}

.el-menu--vertical {
	border-right: none;
	width: 200px;
}

.el-menu--collapse {
	border-right: none;
	width: auto;
}

.container {
	width: 100%;
	height: 100%;
}

.header {
	width: 100%;
	height: 60px;
	border-bottom: 1px solid var(--el-border-color);
	margin: 0;
	padding: 0;
}

.aside {
	width: auto;
	height: 100%;
	border-right: 1px solid var(--el-border-color);
}
</style>
