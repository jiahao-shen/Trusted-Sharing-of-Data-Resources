<script setup lang="ts">
import { DEFAULT_USER_LOGO } from '@/utils/consts'

const props = defineProps<{
	src?: string | null,
	size?: number | string | null
}>()
</script>

<template>
	<el-avatar :size="size" :src="src || DEFAULT_USER_LOGO" />
</template>
