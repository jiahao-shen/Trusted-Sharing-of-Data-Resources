<script setup lang="ts">
import { v4 } from 'uuid'
import { ref, reactive } from 'vue'
import { ElNotification } from 'element-plus'
import { validators } from '@/utils/validators'
import { useRoute, useRouter } from 'vue-router'
import { Plus, Delete } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

const route = useRoute()
const router = useRouter()

const formRef = ref<FormInstance>()
const form = reactive({
	apiName: 'API-1',
	apiID: route.query.apiID,
	orgName: '机构一',
	type: 'sdk',
	validTime: Array(),
	headerList: Array(),
	headerText: '',
	requestList: Array(),
	requestText: '',
	responseList: Array(),
	responseText: '',
	comment: '',
})

const rules = reactive<FormRules>({
	type: [validators.required('调用方式')],
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
})

const requestRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
	type: [validators.required('字段类型')],
	specification: [],
})

const submit = async (formEl: FormInstance | undefined) => {
	if (!formEl) {
		return
	}
	await formEl.validate((valid, fields) => {
		if (valid) {
			ElNotification({
				title: 'API申请成功',
				message: `Token: ${v4()}`,
				type: 'success',
			})
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
	form.headerList = Array()
	form.requestList = Array()
	form.responseList = Array()
}

const responseRules = reactive<FormRules>({
	name: [validators.required('字段名'), validators.notEmpty('字段名')],
	type: [validators.required('字段类型')],
})

const headerType = ref('Form')
const addHeaderItem = () => {
	form.headerList.push({ name: '', required: 'true' })
}

const deleteHeaderItem = (index: number) => {
	form.headerList.splice(index, 1)
	console.log(form.headerList)
}

const requestType = ref('Form')
const addRequestItem = () => {
	form.requestList.push({ name: '', type: '', introduction: '', specification: '', required: 'true' })
}

const deleteRequestItem = (index: number) => {
	form.requestList.splice(index, 1)
}

const responseType = ref('Form')
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
				<span class="text-2xl">API申请</span>
			</template>
			<el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large" id="form">
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
						<el-form-item label="调用方式" prop="type">
							<el-select placeholder="选择" v-model="form.type">
								<el-option label="网页" value="web" />
								<el-option label="SDK" value="sdk" />
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

				<el-row :gutter="60" v-if="form.type === 'web'">
					<el-col :span="5">
						<el-form-item>
							<div class="w-full flex justify-between">
								<label class="table-title">请求头</label>
								<el-radio-group v-model="headerType" size="default">
									<el-radio-button label="Form" />
									<el-radio-button label="Json" />
								</el-radio-group>
							</div>
							<div class="w-full" v-if="headerType === 'Form'">
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
												:icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button class="w-full mt-20px" @click="addHeaderItem" :icon="Plus" />
							</div>

							<div class="w-full mt-10px" v-else-if="headerType === 'Json'">
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
								<el-radio-group v-model="requestType" size="default">
									<el-radio-button label="Form" />
									<el-radio-button label="Json" />
								</el-radio-group>
							</div>

							<div class="w-full" v-if="requestType === 'Form'">
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
												:icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button class="w-full mt-20px" @click="addRequestItem" :icon="Plus" />
							</div>

							<div class="w-full mt-10px" v-else-if="requestType === 'Json'">
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
								<el-radio-group v-model="responseType" size="default">
									<el-radio-button label="Form" />
									<el-radio-button label="Json" />
								</el-radio-group>
							</div>

							<div class="w-full" v-if="responseType === 'Form'">
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
												:icon="Delete"
												circle
												size="default"
											/>
										</template>
									</el-table-column>
								</el-table>
								<el-button class="w-full mt-20px" @click="addResponseItem" :icon="Plus" />
							</div>

							<div class="w-full mt-10px" v-else-if="responseType === 'Json'">
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
		font-size: 1em;
	}
}
</style>
