<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
</script>
<template>
	<div v-if="route.name === '用户管理'">
		<h1>{{ route.name }}</h1>
	</div>
	<router-view v-else />
</template>
<style scoped></style>
