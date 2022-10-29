<script setup lang="ts">
import { ref, onMounted } from 'vue'

const props = defineProps<{
	title: string
	text: string
	item: any
}>()

const emit = defineEmits(['allow'])

const visible = ref(false)

const allow = () => {
	visible.value = false
	emit('allow', props.item)
}
</script>

<template>
	<el-tooltip content="通过">
		<el-button type="success" icon="Check" circle size="default" @click="visible = true" />
	</el-tooltip>
	<el-dialog v-model="visible" :title="title" width="20%" :append-to-body="true">
		<h2 class="text-base">{{ text }}</h2>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="allow">确认</el-button>
		</template>
	</el-dialog>
</template>

<style lang="less" scoped></style>
