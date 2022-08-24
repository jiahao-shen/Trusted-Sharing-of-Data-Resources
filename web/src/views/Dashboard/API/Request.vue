<!-- <template>
	<input type="text" ref="inputRef" />
</template>

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
</script> -->

<template>
  <div class="page-all">
    <div :class="{ all: mode, 'half-1': !mode }">
      <el-button @click="test()">xxx</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'demo',
  data() {
    return {
      mode: true,
    }
  },
  methods: {
    test() {
      this.mode = !this.mode
    },
  },
}
</script>

<style scoped>
.page-all {
	width: 100%;
  height: 100%;
}
.all {
  /* float: left; */
  width: 500px;
  height: 100px;
  background: blue;
  transition: width 2s;
  -webkit-transition: width 2s; /* Safari and Chrome */
}
.half-1 {
  background: blue;
  width: 80%;
  height: 100px;
  transition: width 2s;
  -webkit-transition: width 2s; /* Safari and Chrome */
}
</style>

