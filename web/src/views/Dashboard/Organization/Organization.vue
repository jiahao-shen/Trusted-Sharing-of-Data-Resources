<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
</script>
<template>
	<div v-if="route.name === '机构相关'">
		<h1>机构相关</h1>
	</div>
	<router-view v-else />
</template>
<style scoped></style>
