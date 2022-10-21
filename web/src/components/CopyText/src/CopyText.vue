<script setup lang="ts">
import useClipboard from 'vue-clipboard3'
import { ElMessage } from 'element-plus'

const { toClipboard } = useClipboard()

const props = defineProps<{
	text: string
}>()

const copyText = async () => {
	try {
		await toClipboard(props.text)
		ElMessage({ message: `复制成功`, type: 'success' })
	} catch (err) {
		ElMessage({ message: `复制失败`, type: 'error' })
	}
}
</script>

<template>
	<div class="cursor-pointer" @click="copyText">
		<span>{{ text }}</span>
		<el-icon><CopyDocument /></el-icon>
	</div>
</template>
