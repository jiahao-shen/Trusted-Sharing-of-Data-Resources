<script setup lang="ts">
import moment from 'moment'
import { ref, reactive } from 'vue'
import { validators } from '@/utils/validators'
import { useRoute, useRouter } from 'vue-router'
import type { FormInstance, FormRules, Action } from 'element-plus'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let userList: any[]
let showList = ref(Array())
const addUserDialogVisible = ref(false)

const init = () => {
	userList = [
		{
			username: 'admin',
			password: 'admin',
			permissions: ['权限A', '权限B'],
			createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
		},
	]

	total.value = userList.length
	showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

init()

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const formRef = ref<FormInstance>()
const form = reactive({
	username: '',
	password1: '',
	password2: '',
	permissions: Array(),
})
const rules = reactive<FormRules>({
	username: [validators.required('用户名'), validators.notEmpty('用户名')],
	password1: [validators.required('密码'), validators.lengthRange(6, 20, '密码'), validators.notEmpty('密码')],
	password2: [
		validators.required(''),
		{ 
			validator: (rule: any, value: string, callback: any) => {
				if (form.password1 !== form.password2) {
					callback('两次输入的密码不一致')
				}
			} 
		}],
})

const submit = async (formEl: FormInstance | undefined) => {
	await formEl?.validate((valid, fields) => {
		if (valid) {
			// TODO:
		} else {
			// TODO:
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
				<span class="text-2xl">用户管理</span>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="用户名" prop="username" />
				<el-table-column label="权限" prop="permissions">
					<template #default="scope">
						<el-tag class="mx-5px" v-for="item in scope.row.permission">{{ item }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createdTime" />
				<el-table-column label="操作">
					<template #default scope>
						<div class="w-full h-full flex items-center operate">
							<el-button type="success" icon="Edit" circle size="default" />
							<el-button type="danger" icon="Delete" circle size="default" />
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
					<el-form-item label="用户名" prop="username">
						<el-input placeholder="请输入用户名" v-model="form.username" />
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="60">
				<el-col>
					<el-form-item label="密码" prop="password1">
						<el-input placeholder="请输入密码" v-model="form.password1" show-password/>
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="60">
				<el-col>
					<el-form-item label="确认密码" prop="password2">
						<el-input placeholder="请确认密码" v-model="form.password2" show-password/>
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
<style scoped></style>
