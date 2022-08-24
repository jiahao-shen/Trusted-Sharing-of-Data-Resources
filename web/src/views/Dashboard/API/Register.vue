<script setup lang="ts">
import { v4 } from "uuid"
import { ref, reactive } from "vue"
import { ElNotification } from "element-plus"
import { validators } from "@/utils/validators"
import { Plus, Delete } from "@element-plus/icons-vue"
import type { FormInstance, FormRules } from "element-plus"

const formRef = ref<FormInstance>()
const apiForm = reactive({
	name: "",
	protol: "http",
	url: "",
	method: "get",
	introduction: "",
	category: "1",
	version: "v1.0.0",
	permission: "yes",
	headerList: Array(),
	headerText: "",
	requestList: Array(),
	requestText: "",
	responseList: Array(),
	responseText: "",
})

const apiRules = reactive<FormRules>({
	name: [validators.required("名称"), validators.lengthRange(3, 20, "名称"), validators.notEmpty("名称")],
	url: [validators.required("URL"), validators.url(), validators.notEmpty("URL")],
	// method: [validators.required("请求类型")],
	introduction: [validators.lengthRange(undefined, 200, "功能介绍")],
	category: [validators.required("功能分类")],
	permission: [validators.required("调用授权")],
	version: [
		validators.required("版本信息"),
		validators.lengthRange(undefined, 10, "版本信息"),
		validators.notEmpty("版本信息"),
	],
	headerText: [
		{
			validator: validators.json,
			trigger: "blur",
		},
	],
	requestText: [
		{
			validator: validators.json,
			trigger: "blur",
		},
	],
	responseText: [
		{
			validator: validators.json,
			trigger: "blur",
		},
	],
})

const headerRules = reactive<FormRules>({
	name: [validators.required("字段名"), validators.notEmpty("字段名")],
})

const requestRules = reactive<FormRules>({
	name: [validators.required("字段名"), validators.notEmpty("字段名")],
	type: [validators.required("字段类型")],
	specification: [],
})

const responseRules = reactive<FormRules>({
	name: [validators.required("字段名"), validators.notEmpty("字段名")],
	type: [validators.required("字段类型")],
})

const submit = async (formEl: FormInstance | undefined) => {
	if (!formEl) {
		return
	}
	await formEl.validate((valid, fields) => {
		if (valid) {
			ElNotification({
				title: "API注册成功",
				message: v4(),
				type: "success",
			})
		} else {
			console.error("error", fields)
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

const headerType = ref("Form")
const addHeaderItem = () => {
	apiForm.headerList.push({ name: "", required: "true" })
}

const deleteHeaderItem = (index: number) => {
	apiForm.headerList.splice(index, 1)
	console.log(apiForm.headerList)
}

const requestType = ref("Form")
const addRequestItem = () => {
	apiForm.requestList.push({ name: "", type: "", introduction: "", specification: "", required: "true" })
}

const deleteRequestItem = (index: number) => {
	apiForm.requestList.splice(index, 1)
}

const responseType = ref("Form")
const addResponseItem = () => {
	apiForm.responseList.push({ name: "", type: "", introduction: "" })
}

const deleteResponseItem = (index: number) => {
	apiForm.responseList.splice(index, 1)
}
</script>

<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<span class="text-xl">API注册</span>
			</template>
			<el-form ref="formRef" :model="apiForm" :rules="apiRules" label-position="top" size="large" id="apiForm">
				<el-row :gutter="60">
					<el-col :span="8">
						<el-form-item label="名称" prop="name">
							<el-input placeholder="请输入API名称" v-model="apiForm.name" clearable />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="地址" prop="url">
							<el-input placeholder="请输入URL" v-model="apiForm.url" clearable>
								<template #prepend>
									<el-select placeholder="选择" class="w-100px" v-model="apiForm.protol">
										<el-option label="http://" value="http" />
										<el-option label="https://" value="https" />
									</el-select>
								</template>
							</el-input>
						</el-form-item>
					</el-col>

					<el-col :span="3">
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
								:rows="5"
								type="textarea"
								maxlength="200"
								show-word-limit
								placeholder="请用文字说明API具体功能"
								v-model="apiForm.introduction"
							/>
						</el-form-item>
					</el-col>

					<el-col :span="3">
						<el-form-item label="功能分类" prop="category">
							<el-select placeholder="选择" v-model="apiForm.category">
								<el-option label="1" value="1" />
								<el-option label="2" value="2" />
								<el-option label="3" value="3" />
								<el-option label="4" value="4" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="3">
						<el-form-item label="调用授权" prop="permission">
							<el-select placeholder="选择" v-model="apiForm.permission">
								<el-option label="是" value="yes" />
								<el-option label="否" value="no" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="版本信息" prop="version">
							<el-input placeholder="请输入API版本号" v-model="apiForm.version" clearable/>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="40">
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
								<el-table :data="apiForm.headerList" height="400" empty-text="空" highlight-current-row>
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
										v-model="apiForm.headerText"
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
								<el-table :data="apiForm.requestList" height="400" empty-text="空" highlight-current-row>
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
										v-model="apiForm.requestText"
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
								<el-table :data="apiForm.responseList" height="400" empty-text="空" highlight-current-row>
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
										v-model="apiForm.responseText"
									/>
								</el-form-item>
							</div>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="40" class="m-20px">
					<el-col :span="4" :offset="6">
						<el-button type="success" @click="submit(formRef)" class="w-full">注册</el-button>
					</el-col>
					<el-col :span="4">
						<el-button type="primary" @click="reset(formRef)" class="w-full">重置</el-button>
					</el-col>
					<el-col :span="4">
						<el-button class="w-full">取消</el-button>
					</el-col>
				</el-row>
			</el-form>
		</el-card>
	</div>
</template>

<style scoped>
#apiForm >>> .el-form-item__label {
	font-size: 1rem;
}

.table-title {
	font-size: 1rem;
	color: var(--el-text-color-regular);
}
</style>
