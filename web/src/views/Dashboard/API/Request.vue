<script lang="ts" setup>
import { ref } from 'vue'
import { onMounted } from 'vue'

const inputRef = ref()

onMounted(() => {
	console.log(inputRef) // undefined！页面元素尚未渲染
	console.log(inputRef.value) // 可以拿到 input 元素！
})

const handleFocus = () => {
	inputRef.value.focus() // 成功聚焦
}
</script>

<template>
	<input type="text" ref="inputRef" />
</template>

<style scoped></style>
