<script setup lang="ts">
import { ref, reactive } from 'vue'
import { validators } from '@/utils/validators'
import type { FormInstance, FormRules } from 'element-plus'

const props = defineProps<{
	title: string
	text: string
	item: any
}>()

const emit = defineEmits(['reject'])

const reason = ref('')
const visible = ref(false)

const formRef = ref<FormInstance>()
const form = reactive({
	reason: '',
})

const rules = reactive<FormRules>({
	reason: [validators.required('理由'), validators.notEmpty('理由')],
})

const reject = async (formEl: FormInstance | undefined) => {
	await formEl?.validate((valid, fields) => {
		if (valid) {
			visible.value = false
			emit('reject', props.item, reason.value)
		}
	})
}
</script>

<template>
	<el-tooltip content="驳回">
		<el-button type="danger" icon="Close" circle size="default" @click="visible = true" />
	</el-tooltip>
	<el-dialog v-model="visible" :title="title" width="20%" :append-to-body="true">
		<el-form ref="formRef" :model="form" :rules="rules" id="form">
      <h2 class="text-base">{{ text }}</h2>
			<el-form-item class="mt-20px" prop="reason">
				<el-input :rows="5" type="textarea" maxlength="200" show-word-limit placeholder="请填写理由" v-model="form.reason" />
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="reject(formRef)">确认</el-button>
		</template>
	</el-dialog>
</template>

<style lang="less" scoped></style>
