<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { apiService } from '@/service/api'
import { EnumValues } from 'enum-values'
import { validators } from '@/utils/validators'
import { ElNotification, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules, Action } from 'element-plus'
import { InternetProtocol, HttpMethod, BodyType } from '@/utils/enums'

const router = useRouter()

const formRef = ref<FormInstance>()
const form = reactive({
	name: '',
	protocol: InternetProtocol.HTTP,
	url: '',
	method: HttpMethod.GET,
	introduction: '',
	category: '1',
	version: 'v1.0.0',
	permission: 'yes',
	headerType: BodyType.FORM,
	headerList: Array(),
	headerText: '',
	requestType: BodyType.FORM,
	requestList: Array(),
	requestText: '',
	responseType: BodyType.FORM,
	responseList: Array(),
	responseText: '',
})

const rules = reactive<FormRules>({
	name: [validators.required('名称'), validators.lengthRange(3, 20, '名称'), validators.notEmpty('名称')],
	url: [validators.required('URL'), validators.url(), validators.notEmpty('URL')],
	method: [validators.required('请求类型')],
	introduction: [validators.lengthRange(undefined, 200, '功能介绍')],
	category: [validators.required('功能分类')],
	permission: [validators.required('调用授权')],
	version: [
		validators.required('版本信息'),
		validators.lengthRange(undefined, 10, '版本信息'),
		validators.notEmpty('版本信息'),
	],
	headerText: [
		{
			validator: validators.json,
			trigger: 'blur',
		},
	],
	requestText: [
		{
			validator: validators.json,
			trigger: 'blur',
		},
	],
	responseText: [
		{
			validator: validators.json,
			trigger: 'blur',
		},
	],
})

const headerRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
})

const requestRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
	type: [validators.required('字段类型')],
	specification: [],
})

const responseRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
	type: [validators.required('字段类型')],
})

const submit = async (formEl: FormInstance | undefined) => {
	await formEl?.validate((valid, fields) => {
		if (valid) {
			apiService
				.apiRegisterApply(form)
				.then((res: any) => {
					ElNotification({
						title: 'API注册成功',
						type: 'success',
					})
					router.push('/dashboard/api/register')
				})
				.catch((err: any) => {
					ElNotification({
						title: 'API注册失败',
						message: err.response.data,
						type: 'error',
					})
				})
		} else {
			// TODO:
		}
	})
}

const reset = (formEl: FormInstance | undefined) => {
	formEl?.resetFields()
	form.headerList = Array()
	form.requestList = Array()
	form.responseList = Array()
}

const addHeaderItem = () => {
	form.headerList.push({ name: '', required: 'true' })
}

const deleteHeaderItem = (index: number) => {
	form.headerList.splice(index, 1)
	console.log(form.headerList)
}

const addRequestItem = () => {
	form.requestList.push({ name: '', type: '', introduction: '', specification: '', required: 'true' })
}

const deleteRequestItem = (index: number) => {
	form.requestList.splice(index, 1)
}

const addResponseItem = () => {
	form.responseList.push({ name: '', type: '', introduction: '' })
}

const deleteResponseItem = (index: number) => {
	form.responseList.splice(index, 1)
}
</script>

<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">API注册</h2>
			</template>
			<el-form ref="formRef" :model="form" :rules="rules" label-position="top" id="form">
				<el-row :gutter="60">
					<el-col :span="8">
						<el-form-item label="名称" prop="name">
							<el-input placeholder="请输入API名称" v-model="form.name" clearable />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="地址" prop="url">
							<el-input placeholder="请输入URL" v-model="form.url" clearable>
								<template #prepend>
									<el-select placeholder="选择" class="w-100px" v-model="form.protocol">
										<el-option
											v-for="item in InternetProtocol"
											:value="EnumValues.getNameFromValue(InternetProtocol, item)"
											:label="item"
										/>
									</el-select>
								</template>
							</el-input>
						</el-form-item>
					</el-col>

					<el-col :span="3">
						<el-form-item label="请求类型" prop="method">
							<el-select placeholder="选择" v-model="form.method">
								<el-option
									v-for="item in HttpMethod"
									:value="EnumValues.getNameFromValue(HttpMethod, item)"
									:label="item"
								/>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="10">
						<el-form-item label="功能介绍" prop="introduction">
							<el-input
								:rows="5"
								type="textarea"
								maxlength="200"
								show-word-limit
								placeholder="请用文字说明API具体功能"
								v-model="form.introduction"
							/>
						</el-form-item>
					</el-col>

					<el-col :span="3">
						<el-form-item label="功能分类" prop="category">
							<el-select placeholder="选择" v-model="form.category">
								<el-option label="1" value="1" />
								<el-option label="2" value="2" />
								<el-option label="3" value="3" />
								<el-option label="4" value="4" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="3">
						<el-form-item label="调用授权" prop="permission">
							<el-select placeholder="选择" v-model="form.permission">
								<el-option label="是" value="yes" />
								<el-option label="否" value="no" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="版本信息" prop="version">
							<el-input placeholder="请输入API版本号" v-model="form.version" clearable />
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="5">
						<el-form-item>
							<div class="w-full flex justify-between">
								<label class="table-title">请求头</label>
								<el-radio-group v-model="form.headerType" size="default">
									<el-radio-button v-for="item in BodyType" :label="EnumValues.getNameFromValue(BodyType, item)">
										{{ item }}
									</el-radio-button>
								</el-radio-group>
							</div>
							<div class="w-full" v-if="form.headerType === BodyType.FORM">
								<el-table :data="form.headerList" height="400" empty-text="空" highlight-current-row>
									<el-table-column label="No." type="index" width="60" />
									<el-table-column label="字段名" width="160">
										<template #default="scope">
											<el-form-item :prop="'headerList.' + scope.$index + '.name'" :rules="headerRules.name">
												<el-input class="mt-0" placeholder="请输入字段名" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="必填">
										<template #default="scope">
											<el-form-item :prop="'headerList.' + scope.$index + '.required'">
												<el-select class="mt-0" v-model="scope.row.required">
													<el-option label="是" value="true" />
													<el-option label="否" value="false" />
												</el-select>
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="操作" width="70">
										<template #default="scope">
											<el-button
												@click="deleteHeaderItem(scope.$index)"
												type="danger"
												icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button class="w-full mt-20px" @click="addHeaderItem" icon="Plus" />
							</div>

							<div class="w-full mt-10px" v-else-if="form.headerType === BodyType.JSON">
								<el-form-item prop="headerText">
									<el-input
										placeholder="请输入Header示例(Json格式)"
										type="textarea"
										:rows="21"
										v-model="form.headerText"
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>

					<el-col :span="11">
						<el-form-item>
							<div class="w-full flex justify-between">
								<label class="table-title">请求体</label>
								<el-radio-group v-model="form.requestType" size="default">
									<el-radio-button v-for="item in BodyType" :label="EnumValues.getNameFromValue(BodyType, item)">
										{{ item }}
									</el-radio-button>
								</el-radio-group>
							</div>

							<div class="w-full" v-if="form.requestType === BodyType.FORM">
								<el-table :data="form.requestList" height="400" empty-text="空" highlight-current-row>
									<el-table-column label="No." type="index" width="60" />
									<el-table-column label="字段名" width="150">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.name'" :rules="requestRules.name">
												<el-input placeholder="请输入字段名" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="字段类型" width="160">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.type'" :rules="requestRules.type">
												<el-input placeholder="请输入字段类型" v-model="scope.row.type" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="字段说明" width="180">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.introduction'">
												<el-input placeholder="请简要说明该字段" v-model="scope.row.introduction" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="字段要求" width="250">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.specification'">
												<el-input placeholder="请输入字段要求(建议使用正则表达式)" v-model="scope.row.specification" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="必填">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.required'">
												<el-select class="mt-0" v-model="scope.row.required">
													<el-option label="是" value="true" />
													<el-option label="否" value="false" />
												</el-select>
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="操作" width="70">
										<template #default="scope">
											<el-button
												@click="deleteRequestItem(scope.$index)"
												type="danger"
												icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button class="w-full mt-20px" @click="addRequestItem" icon="Plus" />
							</div>

							<div class="w-full mt-10px" v-else-if="form.requestType === BodyType.JSON">
								<el-form-item prop="requestText">
									<el-input
										placeholder="请输入Requst示例(Json格式)"
										type="textarea"
										:rows="21"
										v-model="form.requestText"
										clearable
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item>
							<div class="w-full flex justify-between">
								<label class="table-title">返回体</label>
								<el-radio-group v-model="form.responseType" size="default">
									<el-radio-button v-for="item in BodyType" :label="EnumValues.getNameFromValue(BodyType, item)">
										{{ item }}
									</el-radio-button>
								</el-radio-group>
							</div>

							<div class="w-full" v-if="form.responseType === BodyType.FORM">
								<el-table :data="form.responseList" height="400" empty-text="空" highlight-current-row>
									<el-table-column label="No." type="index" width="60" />
									<el-table-column label="字段名" width="150">
										<template #default="scope">
											<el-form-item :prop="'responseList.' + scope.$index + '.name'" :rules="responseRules.name">
												<el-input placeholder="请输入字段名" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="字段类型" width="160">
										<template #default="scope">
											<el-form-item :prop="'responseList.' + scope.$index + '.type'" :rules="responseRules.type">
												<el-input placeholder="请输入字段类型" v-model="scope.row.type" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="字段说明">
										<template #default="scope">
											<el-form-item :prop="'responseList.' + scope.$index + '.introduction'">
												<el-input placeholder="请简要说明该字段" v-model="scope.row.introduction" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="操作" width="70">
										<template #default="scope">
											<el-button
												@click="deleteResponseItem(scope.$index)"
												type="danger"
												icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button class="w-full mt-20px" @click="addResponseItem" icon="Plus" />
							</div>

							<div class="w-full mt-10px" v-else-if="form.responseType === BodyType.JSON">
								<el-form-item prop="responseText">
									<el-input
										placeholder="请输入Response示例(Json格式)"
										type="textarea"
										:rows="21"
										v-model="form.responseText"
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60" class="m-20px">
					<el-col :span="4" :offset="6">
						<el-button type="success" @click="submit(formRef)" class="w-full">注册</el-button>
					</el-col>
					<el-col :span="4">
						<el-button type="primary" @click="reset(formRef)" class="w-full">重置</el-button>
					</el-col>
					<el-col :span="4">
						<el-button class="w-full" @click="router.push('/dashboard/api/register')">取消</el-button>
					</el-col>
				</el-row>
			</el-form>
		</el-card>
	</div>
</template>

<style lang="less" scoped>
#form {
	:deep(.el-form-item__label) {
		font-size: 1rem;
	}
}

.table-title {
	font-size: 1rem;
	color: var(--el-text-color-regular);
}
</style>
