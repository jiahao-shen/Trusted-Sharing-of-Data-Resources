<script setup lang="ts">
import { RegisterStatus } from '@/utils/enums'

const props = defineProps<{
	status: string
}>()

const statusToColor = (item: string) => {
	switch (RegisterStatus[item]) {
		case RegisterStatus.PROCESSED:
			return 'text-[var(--el-color-warning)]'
		case RegisterStatus.ALLOW:
			return 'text-[var(--el-color-success)]'
		case RegisterStatus.REJECT:
			return 'text-[var(--el-color-danger)]'
		default:
			break
	}
}
</script>

<template>
	<span :class="statusToColor(status)">{{ RegisterStatus[status] }}</span>
</template>
