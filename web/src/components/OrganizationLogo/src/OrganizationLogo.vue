<script setup lang="ts">
import { DEFAULT_ORG_LOGO } from '@/utils/consts'

const props = defineProps<{
	src?: string | null,
	size?: number | string | null
}>()
</script>

<template>
	<el-avatar shape="square" :size="size" :src="src || DEFAULT_ORG_LOGO" />
</template>
