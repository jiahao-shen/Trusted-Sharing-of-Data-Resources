<script setup lang="ts">
import { ref, reactive } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { validators } from '@/utils/validators'
import { useRoute, useRouter } from 'vue-router'
import { BodyType, APIInvokeMethod } from '@/utils/enums'
import { ElNotification, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules, Action } from 'element-plus'

const route = useRoute()
const router = useRouter()

const formRef = ref<FormInstance>()
const form = reactive({
	apiID: route.query.apiID,
	apiName: route.query.apiName,
	orgName: route.query.orgName,
	comment: '',
	invokeType: APIInvokeMethod.WEB,
	validTime: Array(),
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
	invokeType: [validators.required('调用方式')],
	validTime: [validators.required('有效期')],
	comment: [validators.required('备注说明')],
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
	value: [validators.required('值')],
})

const requestRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
	value: [validators.required('值')],
})

const responseRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
	value: [validators.required('值')],
})

const submit = async (formEl: FormInstance | undefined) => {
	await formEl?.validate((valid, fields) => {
		if (valid) {
			apiService
				.apiInvokeApply(form)
				.then((res: any) => {
					ElMessageBox.alert(res.data, 'API申请调用成功', {
						confirmButtonText: '确认',
						callback: (action: Action) => {},
					})
				})
				.catch((err: any) => {
					ElNotification({
						title: 'API申请调用失败',
						message: err.reponse.data,
						type: 'error',
					})
				})
		} else {
			console.error('error', fields)
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
	form.headerList.push({ name: '', value: '' })
}

const deleteHeaderItem = (index: number) => {
	form.headerList.splice(index, 1)
	console.log(form.headerList)
}

const addRequestItem = () => {
	form.requestList.push({ name: '', value: '' })
}

const deleteRequestItem = (index: number) => {
	form.requestList.splice(index, 1)
}

const addResponseItem = () => {
	form.responseList.push({ name: '', value: '' })
}

const deleteResponseItem = (index: number) => {
	form.responseList.splice(index, 1)
}
</script>

<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">API申请</h2>
			</template>
			<el-form ref="formRef" :model="form" :rules="rules" label-position="top" id="form">
				<el-row :gutter="60">
					<el-col :span="6">
						<el-form-item label="API名称" required>
							<el-input v-model="form.apiName" disabled />
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item label="API ID" required>
							<el-input v-model="form.apiID" disabled />
						</el-form-item>
					</el-col>

					<el-col :span="6">
						<el-form-item label="所属机构" required>
							<el-input v-model="form.orgName" disabled />
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="10">
						<el-form-item label="备注说明" prop="comment">
							<el-input
								:rows="5"
								type="textarea"
								maxlength="200"
								show-word-limit
								placeholder="请用文字描述"
								v-model="form.comment"
							/>
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="调用方式" prop="invokeType">
							<el-select placeholder="选择" v-model="form.invokeType">
								<el-option
									v-for="item in APIInvokeMethod"
									:label="item"
									:value="EnumValues.getNameFromValue(APIInvokeMethod, item)"
								/>
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="6">
						<el-form-item label="有效期" prop="validTime">
							<el-date-picker
								v-model="form.validTime"
								type="datetimerange"
								start-placeholder="请选择开始时间"
								end-placeholder="请选择结束时间"
								:default-time="[new Date(), new Date()]"
							/>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60" v-if="form.invokeType === APIInvokeMethod.SDK">
					<el-col :span="8">
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
									<el-table-column label="字段名" width="300">
										<template #default="scope">
											<el-form-item :prop="'headerList.' + scope.$index + '.name'" :rules="headerRules.name">
												<el-input class="mt-0" placeholder="请输入字段名" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>
									<el-table-column label="值">
										<template #default="scope">
											<el-form-item :prop="'headerList.' + scope.$index + '.value'" :rules="headerRules.value">
												<el-input class="mt-0" placeholder="请输入值" v-model="scope.row.value" />
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

					<el-col :span="8">
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
									<el-table-column label="字段名" width="300">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.name'" :rules="requestRules.name">
												<el-input placeholder="请输入字段名" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="值">
										<template #default="scope">
											<el-form-item :prop="'requestList.' + scope.$index + '.value'" :rules="requestRules.value">
												<el-input class="mt-0" placeholder="请输入值" v-model="scope.row.value" />
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
									<el-table-column label="字段名" width="300">
										<template #default="scope">
											<el-form-item :prop="'responseList.' + scope.$index + '.name'" :rules="responseRules.name">
												<el-input placeholder="请输入字段名" v-model="scope.row.name" />
											</el-form-item>
										</template>
									</el-table-column>

									<el-table-column label="值">
										<template #default="scope">
											<el-form-item :prop="'responseList.' + scope.$index + '.value'" :rules="responseRules.value">
												<el-input class="mt-0" placeholder="请输入值" v-model="scope.row.value" />
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
						<el-button type="success" @click="submit(formRef)" class="w-full">申请调用</el-button>
					</el-col>
					<el-col :span="4">
						<el-button type="primary" @click="reset(formRef)" class="w-full">重置</el-button>
					</el-col>
					<el-col :span="4">
						<el-button class="w-full" @click="router.push('/dashboard/api/request')">取消</el-button>
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
</style>
