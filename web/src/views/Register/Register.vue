<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'
import { validators } from '@/utils/validators'
import { useRoute, useRouter } from 'vue-router'
import { regionData, CodeToText } from 'element-china-area-data'
import type { FormInstance, FormRules, UploadProps, UploadInstance } from 'element-plus'

const route = useRoute()
const router = useRouter()

const active = ref(0)
const formRef = ref<FormInstance>()
const form = reactive({
	logo: [],
	name: '',
	type: '',
	telephone: '',
	email: '',
	city: [],
	address: '',
	introduction: '',
	superior: '',
	provideNode: 'no',
	numNodes: 0,
	file: [],
})

const rules = reactive<FormRules>({
	name: [validators.required('机构名称'), validators.notEmpty('机构名称')],
	type: [validators.required('机构类型')],
	telephone: [validators.required('机构电话'), validators.notEmpty('机构电话'), validators.telephone()],
	email: [validators.required('机构邮箱'), validators.notEmpty('机构邮箱'), validators.email()],
	city: [validators.required('所在城市')],
	address: [validators.required('详细地址'), validators.notEmpty('详细地址')],
})

const logoSizeLimit = 2
const logoAcceptFormat = ['image/jpg', 'image/jpeg', 'image/png']
const logoRef = ref<UploadInstance>()
const logoPreviewURL = ref('')
const previewDialogVisible = ref(false)

const uploadLogoSuccess: UploadProps['onSuccess'] = (response, uploadFile, uploadFiles) => {
	console.log('Success')
}

const beforeLogoUpload: UploadProps['beforeUpload'] = (rawFile) => {
	if (rawFile.size / 1024 / 1024 > logoSizeLimit) {
		ElMessage.error(`Logo大小不能超过${logoSizeLimit}MB`)
	}
}

const onLogoChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
	console.log('onLogoChange')
}

const onLogoPreview: UploadProps['onPreview'] = (uploadFile) => {
	if (uploadFile.raw) {
		logoPreviewURL.value = URL.createObjectURL(uploadFile.raw)
		previewDialogVisible.value = true
	}
}

const fileSizeLimit = 10
const fileAcceptFormat = ['.rar', '.zip']
const fileRef = ref<UploadInstance>()

const uploadFileSuccess: UploadProps['onSuccess'] = (response, uploadFile, uploadFiles) => {
	console.log('Success')
}

const beforeFileUpload: UploadProps['beforeUpload'] = (rawFile) => {
	if (rawFile.size / 1024 / 1024 > fileSizeLimit) {
		ElMessage.error(`文件大小不能超过${fileSizeLimit}MB`)
	}
}

const onFileChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
	console.log('onFileChange')
}

const submit = async (formEl: FormInstance | undefined) => {
	console.log(form)
	await formEl?.validate((valid, fields) => {
		if (valid) {
			// TODO:
		} else {
			// TODO:
		}
	})
}

const reset = (formEl: FormInstance | undefined) => {
	formEl?.resetFields()
	logoRef.value?.clearFiles()
	fileRef.value?.clearFiles()
}
</script>

<template>
	<div class="flex w-full h-full justify-center items-center">
		<el-card class="w-800px">
			<template #header>
				<span class="text-2xl">机构注册</span>
			</template>
			<el-form class="w-full" ref="formRef" :model="form" :rules="rules" label-position="top" id="form">
				<el-row :gutter="60">
					<el-col :offset="6" :span="12">
						<el-form-item>
							<div class="flex flex-col justify-center items-center">
								<el-upload
									v-model:file-list="form.logo"
									ref="logoRef"
									class="w-150px h-150px"
									action=""
									list-type="picture-card"
									:limit="1"
									:show-file-list="true"
									:on-change="onLogoChange"
									:on-success="uploadLogoSuccess"
									:on-preview="onLogoPreview"
									:before-upload="beforeLogoUpload"
									:accept="logoAcceptFormat.join(',')"
									:auto-upload="false"
								>
									<el-icon :size="50" color="#8c939d"><Plus /></el-icon>
								</el-upload>
								<label class="table-title">请上传Logo</label>
							</div>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="16">
						<el-form-item label="机构名称" prop="name">
							<el-input placeholder="请输入机构名称" v-model="form.name" clearable />
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item label="机构类型" prop="type">
							<el-select placeholder="选择" v-model="form.type">
								<el-option label="医疗" value="medical" />
								<el-option label="教育" value="education" />
								<el-option label="金融" value="financial" />
								<el-option label="政府" value="government" />
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="8">
						<el-form-item label="机构电话" prop="telephone">
							<el-input placeholder="请输入机构电话" v-model="form.telephone">
								<template #prefix>
									<el-icon><Phone /></el-icon>
								</template>
							</el-input>
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item label="机构邮箱" prop="email">
							<el-input placeholder="请输入机构邮箱" v-model="form.email">
								<template #prefix>
									<el-icon><Message /></el-icon>
								</template>
							</el-input>
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item label="所在城市" prop="city">
							<el-cascader v-model="form.city" placeholder="请选择省/市/区" :options="regionData" filterable />
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col>
						<el-form-item label="详细地址" prop="address">
							<el-input placeholder="请输入详细地址" v-model="form.address"></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col>
						<el-form-item label="机构介绍" prop="introduction">
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
				</el-row>

				<el-row :gutter="60">
					<el-col :span="10">
						<el-form-item class="w-full" label="上级机构" >
							<el-select class="w-full" placeholder="选择" v-model="form.superior">
								<el-option label="无" value="" />
								<el-option label="机构1" value="Org1" />
								<el-option label="机构2" value="Org2" />
								<el-option label="机构3" value="Org3" />
								<el-option label="机构4" value="Org4" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="6">
						<el-form-item class="w-full" label="提供节点">
							<el-radio-group class="w-full" v-model="form.provideNode">
								<el-radio-button label="no">否</el-radio-button>
								<el-radio-button label="yes">是</el-radio-button>
							</el-radio-group>
						</el-form-item>
					</el-col>

					<el-col :span="6" v-if="form.provideNode === 'yes'">
						<el-form-item label="节点数量">
							<el-select placeholder="选择" v-model="form.numNodes">
								<el-option label="1" value="1" />
								<el-option label="2" value="2" />
								<el-option label="3" value="3" />
								<el-option label="4" value="4" />
								<el-option label="5" value="5" />
								<el-option label="6" value="6" />
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col>
						<el-form-item label="上传证明文件">
							<el-upload
								ref="fileRef"
								class="w-full"
								action=""
								drag
								:limit="1"
								:accept="fileAcceptFormat.join(',')"
								:on-success="uploadFileSuccess"
								:before-upload="beforeFileUpload"
								:auto-upload="false"
								v-model:file-list="form.file"
							>
								<el-icon class="el-icon--upload"><UploadFilled /></el-icon>
								<div class="el-upload__text">拖拽到此或<em>点击上传</em></div>
								<template #tip>
									<div v-if="form.file.length === 0" class="el-upload__tip">请上传压缩包(ZIP或者RAR)</div>
								</template>
							</el-upload>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="60">
					<el-col :span="6" :offset="3">
						<el-button type="success" @click="submit(formRef)" class="w-full">注册</el-button>
					</el-col>
					<el-col :span="6">
						<el-button type="primary" @click="reset(formRef)" class="w-full">重置</el-button>
					</el-col>
					<el-col :span="6">
						<el-button class="w-full" @click="router.push('/login')">返回</el-button>
					</el-col>
				</el-row>
			</el-form>
		</el-card>
	</div>

	<el-dialog v-model="previewDialogVisible">
		<el-image class="w-full" :src="logoPreviewURL" />
	</el-dialog>
</template>

<style lang="less" scoped>
#form {
	:deep(.el-form-item__label) {
		font-size: 1rem;
	}
	:deep(.el-form-item__content) {
		display: flex;
		justify-content: center;
	}
	:deep(.el-upload--picture-card) {
		display: v-bind("form.logo.length ? 'none': 'inline-flex'");
	}
	:deep(.el-upload-list__item.is-ready) {
		margin: 0;
	}
	:deep(.el-upload.el-upload--text.is-drag) {
		display: v-bind("form.file.length ? 'none' : 'block'");
	}
}

.table-title {
	font-size: 1rem;
	color: var(--el-text-color-regular);
}
</style>
