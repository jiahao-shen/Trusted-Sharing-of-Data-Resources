<script setup lang="ts">
import { ref, reactive } from 'vue'
import { userService } from '@/service/user'
import { ElNotification } from 'element-plus'
import { validators } from '@/utils/validators'
import type { FormInstance, FormRules } from 'element-plus'

const props = defineProps<{
	title: string
	visible: boolean
	organization: string
}>()

const emit = defineEmits(['close', 'success', 'failed'])

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

const handleClose = (done: () => void) => {
	formRef.value?.resetFields()
	done()
}

const submit = async (formEl: FormInstance | undefined) => {
	await formEl?.validate((valid, fields) => {
    console.log('validate')
		if (valid) {
			console.log('valid')
			userService
				.userRegister(form, props.organization)
				.then((res: any) => {
					if (res.data) {
						ElNotification({
							title: '新增用户成功',
							type: 'success',
						})
            emit('success')
					} else {
						ElNotification({
							title: '新增用户失败',
							type: 'error',
						})
            emit('failed')
					}
				})
				.catch((err: any) => {
				})
		} else {
		}
	})
}
</script>

<template>
	<el-dialog v-model="visible" :title="title" :before-close="handleClose" width="40%">
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
					<el-button class="w-full" type="danger" @click="emit('close')">返回</el-button>
				</el-col>
			</el-row>
		</el-form>
	</el-dialog>
</template>
