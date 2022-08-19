<script setup lang="ts">
import type { FormRules } from "element-plus"
import { ref, reactive } from "vue"
import { required } from '@/utils/validators.ts"

const loginForm = reactive({
	username: "",
	password: "",
})

const rules = reactive<FormRules>({
	username: required(),
	password: required(),
})

const remeber = ref()
</script>
<template>
	<div class="flex w-full h-full">
		<div class="flex w-[60%] h-full justify-center items-center">
			<div>
				<img class="w-400px" src="@/assets/img/login-box-bg.svg" />
				<div class="text-20px text-bold text-center">欢迎来到数据可信共享平台</div>
			</div>
		</div>
		<div class="flex w-[40%] h-full justify-center items-center">
			<el-card class="w-[80%]">
				<div class="text-center m-20px">
					<h1 class="text-2xl">登 录</h1>
				</div>
				<el-form class="p-10px" label-position="top" size="large" :model="loginForm">
					<el-form-item label="用户名" class="my-10px">
						<el-input placeholder="请输入用户名" v-model="loginForm.username" clearable />
					</el-form-item>
					<el-form-item label="密码" class="my-10px">
						<el-input placeholder="请输入密码" v-model="loginForm.password" clearable show-password />
					</el-form-item>
					<div class="w-full my-10px flex justify-between">
						<el-checkbox v-model="remeber" label="记住我" />
						<el-link type="primary" :underline="false">忘记密码</el-link>
					</div>
					<div class="w-full my-10px">
						<el-button class="w-full" type="primary">登录</el-button>
					</div>
					<div class="w-full my-10px">
						<el-button class="my-10px w-full">注册</el-button>
					</div>
					<el-divider>
						<span class="text-base font-normal">其他登录方式</span>
					</el-divider>
					<div class="w-full m-10px px-30px py-10px flex justify-between">
						<el-icon :size="40" class="cursor-pointer">
							<span class="iconify" data-icon="ant-design:github-filled" data-inline="false" style="color: #999" />
						</el-icon>
						<el-icon :size="40" class="cursor-pointer">
							<span class="iconify" data-icon="ant-design:qq-circle-filled" data-inline="false" style="color: #999" />
						</el-icon>
						<el-icon :size="40" class="cursor-pointer">
							<span class="iconify" data-icon="ant-design:wechat-filled" data-inline="false" style="color: #999" />
						</el-icon>
						<el-icon :size="40" class="cursor-pointer">
							<span
								class="iconify"
								data-icon="ant-design:alipay-circle-filled"
								data-inline="false"
								style="color: #999"
							/>
						</el-icon>
					</div>
				</el-form>
			</el-card>
		</div>
	</div>
</template>

<style scoped>
.iconify:hover {
	color: var(--el-color-primary) !important;
}
</style>
