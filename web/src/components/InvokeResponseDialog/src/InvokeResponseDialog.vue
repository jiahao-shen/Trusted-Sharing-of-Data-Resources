<script setup lang="ts">
const props = defineProps<{
  visible: boolean, 
  title: string,
	response: any,
}>()

const emit = defineEmits(['close'])
</script>

<template>
	<el-dialog v-model="visible" :title="title" width="40%">
		<json-viewer :value="response" copyable sort/>

		<template #footer>
			<el-button type="primary" @click="emit('close')">确认</el-button>
		</template>
	</el-dialog>
</template>
