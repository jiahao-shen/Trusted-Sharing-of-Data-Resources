<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Delete, Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

const formRef = ref<FormInstance>()
const apiForm = reactive({
	name: '',
	protol: 'http',
	url: '',
	method: 'get',
	introduction: '',
	category: '1',
	version: 'v1.0.0',
	permission: 'yes',
	headerList: Array(),
	headerText: '',
	requestList: Array(),
	requestText: '',
	responseList: Array(),
	responseText: '',
})

const regexURL = /^[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b(?:[-a-zA-Z0-9()@:%_\+.~#?&//=]*)$/

const jsonValidator = (rule: any, value: any, callback: any) => {
	try {
		JSON.parse(value)
		callback()
	} catch (e) {
		callback('Json格式错误')
	}
}

const apiRules = reactive<FormRules>({
	name: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
		{
			min: 3,
			max: 20,
			message: '字数需要在3-20之间',
			trigger: 'blur',
		},
	],
	url: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
		{
			validator: (rule: any, value: any, callback: any) => {
				if (!regexURL.test(value)) {
					callback(new Error('URL格式错误'))
				} else {
					callback()
				}
			},
			trigger: 'blur',
		},
	],
	method: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
	introduction: [
		{
			max: 200,
			message: '字数不能超过200',
			trigger: 'blur',
		},
	],
	category: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
	version: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
		{
			max: 10,
			message: '长度不能超过10',
		},
	],
	permission: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
	headerText: [
		{
			validator: jsonValidator,
			trigger: 'blur',
		},
	],
	requestText: [
		{
			validator: jsonValidator,
			trigger: 'blur',
		},
	],
	responseText: [
		{
			validator: jsonValidator,
			trigger: 'blur',
		},
	],
})

const headerRules = reactive<FormRules>({
	name: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
})

const requestRules = reactive<FormRules>({
	name: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
	type: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
})

const responseRules = reactive<FormRules>({
	name: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
	type: [
		{
			required: true,
			message: '不能为空',
			trigger: 'blur',
		},
	],
})

const submit = async (formEl: FormInstance | undefined) => {
	if (!formEl) {
		return
	}
	await formEl.validate((valid, fields) => {
		if (valid) {
			console.log(apiForm)
		} else {
			console.error('error', fields)
		}
	})
}

const reset = (formEl: FormInstance | undefined) => {
	if (!formEl) {
		return
	}
	formEl.resetFields()
	apiForm.headerList = Array()
	apiForm.requestList = Array()
	apiForm.responseList = Array()
}

const headerType = ref('Form')
const addHeaderItem = () => {
	apiForm.headerList.push({ name: '', required: 'true' })
}

const deleteHeaderItem = (index: number) => {
	apiForm.headerList.splice(index, 1)
}

const requestType = ref('Form')
const addRequestItem = () => {
	apiForm.requestList.push({ name: '', type: '', introduction: '', require: '' })
}

const deleteRequestItem = (index: number) => {
	apiForm.requestList.splice(index, 1)
}

const responseType = ref('Form')
const addResponseItem = () => {
	apiForm.responseList.push({ name: '', type: '', introduction: '' })
}

const deleteResponseItem = (index: number) => {
	apiForm.responseList.splice(index, 1)
}
</script>

<template>
	<div style="display: flex; width: 100%; height: 100%">
		<div style="width: 80%; height: 100%; margin: 20px">
			<h2>API注册</h2>

			<el-form ref="formRef" :model="apiForm" :rules="apiRules" label-position="top" size="large" id="apiForm">
				<el-row :gutter="60">
					<el-col :span="6">
						<el-form-item label="名称" prop="name">
							<el-input placeholder="请输入API名称" v-model="apiForm.name" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="地址" prop="url">
							<el-input placeholder="请输入URL" v-model="apiForm.url">
								<template #prepend>
									<el-select placeholder="选择" style="width: 100px" v-model="apiForm.protol">
										<el-option label="http://" value="http" />
										<el-option label="https://" value="https" />
									</el-select>
								</template>
							</el-input>
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="请求类型" prop="method">
							<el-select placeholder="选择" v-model="apiForm.method">
								<el-option label="GET" value="get" />
								<el-option label="POST" value="post" />
								<el-option label="PUT" value="put" />
								<el-option label="DELETE" value="delete" />
								<el-option label="TRACE" value="trace" />
								<el-option label="CONNECT" value="connect" />
								<el-option label="HEAD" value="head" />
								<el-option label="OPTIONS" value="options" />
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="10">
						<el-form-item label="功能介绍" prop="introduction">
							<el-input
								:rows="4"
								type="textarea"
								maxlength="200"
								show-word-limit
								placeholder="请用文字说明API具体功能"
								v-model="apiForm.introduction"
							/>
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="功能分类" prop="category">
							<el-select placeholder="选择" v-model="apiForm.category">
								<el-option label="1" value="1" />
								<el-option label="2" value="2" />
								<el-option label="3" value="3" />
								<el-option label="4" value="4" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="版本信息" prop="version">
							<el-input placeholder="请输入API版本号" v-model="apiForm.version" />
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="调用授权" prop="permission">
							<el-select placeholder="选择" v-model="apiForm.permission">
								<el-option label="是" value="yes" />
								<el-option label="否" value="no" />
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="40">
					<el-col :span="6">
						<el-form-item>
							<div style="width: 100%; display: flex; justify-content: space-between">
								<label class="table-title">请求头</label>
								<el-radio-group v-model="headerType" size="default">
									<el-radio-button label="Form" />
									<el-radio-button label="Json" />
								</el-radio-group>
							</div>
							<div style="width: 100%" v-if="headerType == 'Form'">
								<el-table :data="apiForm.headerList" height="500" empty-text="空">
									<el-table-column label="关键字" width="250">
										<template #default="scope">
											<el-form-item :prop="'header.' + scope.$index + '.name'" :rules="headerRules.name">
												<el-input style="margin-top: 0px" placeholder="请输入关键字" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="必填">
										<template #default="scope">
											<el-form-item :prop="'header.' + scope.$index + '.required'" :show-message="false">
												<el-select style="margin-top: 0px" v-model="scope.row.required">
													<el-option label="是" value="true" />
													<el-option label="否" value="false" />
												</el-select>
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="操作" width="70">
										<template #default="scope" id="fuck">
											<el-button
												@click="deleteHeaderItem(scope.$index)"
												type="danger"
												:icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button style="width: 100%; margin-top: 20px" @click="addHeaderItem" :icon="Plus" />
							</div>

							<div style="width: 100%; margin-top: 20px" v-else-if="headerType == 'Json'">
								<el-form-item prop="headerText">
									<el-input
										placeholder="请输入Header示例(Json格式)"
										type="textarea"
										:rows="10"
										v-model="apiForm.headerText"
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>

					<el-col :span="10">
						<el-form-item>
							<div style="width: 100%; display: flex; justify-content: space-between">
								<label class="table-title">请求体</label>
								<el-radio-group v-model="requestType" size="default">
									<el-radio-button label="Form" />
									<el-radio-button label="Json" />
								</el-radio-group>
							</div>

							<div style="width: 100%" v-if="requestType == 'Form'">
								<el-table :data="apiForm.requestList" height="500" empty-text="空">
									<el-table-column label="字段名" width="150">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.name'" :rules="requestRules.name">
												<el-input placeholder="请输入字段名称" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="字段类型" width="150">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.type'" :rules="requestRules.type">
												<el-input placeholder="请输入字段类型" v-model="scope.row.type" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="字段说明" width="180">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.introduction'">
												<el-input placeholder="请简要说明该字段" v-model="scope.row.introduction" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="字段要求">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.require'">
												<el-input placeholder="请输入字段要求(建议使用正则表达式)" v-model="scope.row.require" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="操作" width="70">
										<template #default="scope">
											<el-button
												@click="deleteRequestItem(scope.$index)"
												type="danger"
												:icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button style="width: 100%; margin-top: 20px" @click="addRequestItem" :icon="Plus" />
							</div>

							<div style="width: 100%; margin-top: 20px" v-else-if="requestType == 'Json'">
								<el-form-item prop="requestText">
									<el-input
										placeholder="请输入Requst示例(Json格式)"
										type="textarea"
										:rows="10"
										v-model="apiForm.requestText"
										clearable
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item>
							<div style="width: 100%; display: flex; justify-content: space-between">
								<label class="table-title">返回体</label>
								<el-radio-group v-model="responseType" size="default">
									<el-radio-button label="Form" />
									<el-radio-button label="Json" />
								</el-radio-group>
							</div>

							<div style="width: 100%" v-if="responseType == 'Form'">
								<el-table :data="apiForm.responseList" height="500" empty-text="空">
									<el-table-column label="字段名" width="150">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.name'" :rules="responseRules.name">
												<el-input placeholder="请输入字段名称" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="字段类型" width="150">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.type'" :rules="responseRules.type">
												<el-input placeholder="请输入字段类型" v-model="scope.row.type" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="字段说明">
										<template #default="scope">
											<el-form-item :prop="'request.' + scope.$index + '.introduction'">
												<el-input placeholder="请简要说明该字段" v-model="scope.row.introduction" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="操作" width="70">
										<template #default="scope">
											<el-button
												@click="deleteResponseItem(scope.$index)"
												type="danger"
												:icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button style="width: 100%; margin-top: 20px" @click="addResponseItem" :icon="Plus" />
							</div>

							<div style="width: 100%; margin-top: 20px" v-else-if="responseType == 'Json'">
								<el-form-item prop="responseText">
									<el-input
										placeholder="请输入Response示例(Json格式)"
										type="textarea"
										:rows="10"
										v-model="apiForm.responseText"
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="40" style="margin: 20px">
					<el-col :span="4" :offset="6">
						<el-button type="success" @click="submit(formRef)" style="width: 100%">注册</el-button>
					</el-col>
					<el-col :span="4">
						<el-button type="primary" @click="reset(formRef)" style="width: 100%">重置</el-button>
					</el-col>
					<el-col :span="4">
						<el-button style="width: 100%">取消</el-button>
					</el-col>
				</el-row>
			</el-form>
		</div>
		<div style="width: 20%; height: 100%; background-color: gray"></div>
	</div>
</template>

<style scoped>
#apiForm >>> .el-form-item__label {
	font-size: 1rem;
}

h2 {
	margin-top: 0;
}

.table-title {
	font-size: 1rem;
	color: var(--el-text-color-regular);
}
</style>
