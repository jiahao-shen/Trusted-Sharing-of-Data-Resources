<script setup lang="ts"></script>

<template>
	<div class="w-full h-full flex">
		<div class="w-250px h-full flex flex-col">
			<div class="w-full !h-60px p-5px flex justify-between items-center border-b-1 border-[var(--el-border-color)]">
				<el-image class="h-full" src="/src/assets/img/logo.svg" />
				<h1 class="text-xl">Hawk Eye</h1>
			</div>
			<el-menu class="flex-grow" :collapse="false" router>
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
		</div>
		<div class="w-full h-full flex-col">
			<div class="w-full h-60px border-b-1 border-[var(--el-border-color)] flex justify-between">
				<div class="h-full p-10px flex justify-center items-center">
					<el-icon :size="30" class="cursor-pointer" style="color: #999" >
						<span class="iconify" data-icon="ant-design:menu-fold-outlined" data-inline="false"/>
					</el-icon>
          <el-breadcrumb class="px-10px" separator="/">
            <el-breadcrumb-item to="/login">Hello</el-breadcrumb-item>
            <el-breadcrumb-item>World</el-breadcrumb-item>
          </el-breadcrumb>
				</div>

				<div class="h-full bg-green-100"></div>
			</div>
			<div class="w-full h-full flex-grow"></div>
		</div>
	</div>
</template>

<style scoped></style>
