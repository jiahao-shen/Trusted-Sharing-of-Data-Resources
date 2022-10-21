<script setup lang="ts">
import moment from 'moment'
import { useAppStore } from '@/store/app'
import { userService } from '@/service/user'
import { ElNotification } from 'element-plus'
import { ref, reactive, onMounted } from 'vue'
import { validators } from '@/utils/validators'
import { useRoute, useRouter } from 'vue-router'
import type { FormInstance, FormRules, Action } from 'element-plus'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let userList: any[]
let showList = ref(Array())
const addUserDialogVisible = ref(false)

onMounted(() => {
	userService
		.userList()
		.then((res: any) => {
			console.log(res.data)
			userList = res.data
			total.value = userList.length
			showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
			ElNotification({
				title: '未知错误',
				message: err.response.data,
				type: 'error',
			})
		})
})

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const formRef = ref<FormInstance>()
const form = reactive({
	id: '',
	password1: '',
	password2: '',
	permissions: Array(),
})
const rules = reactive<FormRules>({
	id: [
		validators.required('账号'),
		validators.notEmpty('账号'),
		{
			validator: (rule: any, value: any, callback: any) => {
				userService
					.userExist(form.id)
					.then((res: any) => {
						if (res.data) {
							callback('该账号已存在')
						} else {
							callback()
						}
					})
					.catch((err: any) => {
						ElNotification({
							title: '网络异常',
							message: err.response.data,
							type: 'error',
						})
					})
			},
			trigger: 'blur',
		},
	],
	password1: [validators.required('密码'), validators.lengthRange(6, 20, '密码'), validators.notEmpty('密码')],
	password2: [
		validators.required(''),
		{
			validator: (rule: any, value: string, callback: any) => {
				if (form.password1 !== form.password2) {
					callback('两次输入的密码不一致')
				} else {
					callback()
				}
			},
			trigger: 'trigger',
		},
	],
})

const submit = async (formEl: FormInstance | undefined) => {
	await formEl?.validate((valid, fields) => {
		if (valid) {
			console.log('valid')
			userService
				.userRegister(form)
				.then((res: any) => {
					if (res.data) {
						ElNotification({
							title: '注册成功',
							type: 'success',
						})
						addUserDialogVisible.value = false
						userService
							.userList()
							.then((res: any) => {
								console.log(res.data)
								userList = res.data
								total.value = userList.length
								showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
							})
							.catch((err: any) => {
								console.error(err)
							})
					} else {
						ElNotification({
							title: '注册失败',
							type: 'error',
						})
					}
				})
				.catch((err: any) => {
					ElNotification({
						title: '网络异常',
						message: err.response.data,
						type: 'error',
					})
				})
		} else {
			// TODO:
			console.log('fuck')
		}
	})
}

const handleClose = (done: () => void) => {
	formRef.value?.resetFields()
	done()
}
</script>
<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">用户管理</h2>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="账号" prop="id" />
				<el-table-column label="用户名" prop="username" />
				<el-table-column label="权限" prop="permissions">
					<template #default="scope">
						<el-tag class="mx-5px" v-for="item in scope.row.permission">{{ item }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="创建时间">
					<template #default="scope">
						{{ moment(scope.row.createdTime).format('YYYY-MM-DD HH:mm:ss') }}
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center operate">
							<el-tooltip content="编辑">
								<el-button type="success" icon="Edit" circle size="default" />
							</el-tooltip>
							<el-tooltip content="删除">
								<el-button type="danger" icon="Delete" circle size="default" />
							</el-tooltip>
						</div>
					</template>
				</el-table-column>
			</el-table>

			<div class="w-full mt-20px flex items-center justify-center">
				<el-pagination
					layout="jumper, prev, pager, next,total"
					:total="total"
					:page-size="pageSize"
					@current-change="handleCurrentChange"
				/>
			</div>

			<el-button class="w-full mt-20px" plain @click="addUserDialogVisible = true">
				<el-icon><Plus /></el-icon>
				&nbsp新增用户
			</el-button>
		</el-card>
	</div>

	<el-dialog v-model="addUserDialogVisible" title="新增用户" :before-close="handleClose" width="40%">
		<el-form ref="formRef" :model="form" :rules="rules" label-position="top" id="form">
			<el-row :gutter="60">
				<el-col>
					<el-form-item label="账号" prop="id">
						<el-input placeholder="请输入账号" v-model="form.id" />
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="60">
				<el-col>
					<el-form-item label="密码" prop="password1">
						<el-input placeholder="请输入密码" v-model="form.password1" show-password />
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="60">
				<el-col>
					<el-form-item label="确认密码" prop="password2">
						<el-input placeholder="请确认密码" v-model="form.password2" show-password />
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="60">
				<el-col>
					<el-form-item label="用户权限" prop="permissions">
						<el-checkbox-group v-model="form.permissions" size="large">
							<el-checkbox label="权限一" />
							<el-checkbox label="权限二" />
							<el-checkbox label="权限三" />
							<el-checkbox label="权限四" />
							<el-checkbox label="权限五" />
						</el-checkbox-group>
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="60">
				<el-col :span="6" :offset="3">
					<el-button class="w-full" type="success" @click="submit(formRef)">确认</el-button>
				</el-col>

				<el-col :span="6">
					<el-button class="w-full" type="primary" @click="formRef?.resetFields()">重置</el-button>
				</el-col>

				<el-col :span="6">
					<el-button class="w-full" type="danger" @click="addUserDialogVisible = false">返回</el-button>
				</el-col>
			</el-row>
		</el-form>
	</el-dialog>
</template>
<style lang="less" scoped>
#form {
	:deep(.el-form-item__label) {
		font-size: 1rem;
	}
}
</style>
