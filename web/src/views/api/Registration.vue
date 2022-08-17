<script setup lang="ts">
import { reactive } from 'vue'

const form = reactive({
	name: '',
  protol: '',
	url: '',
	method: '',
})

function onSubmit() {
  console.log(form)
}
</script>

<template>
	<h2>API注册</h2>

	<el-form :model="form" label-position="top">
		<el-row :gutter="40">
			<el-col :span="4">
				<h3>名称</h3>
				<el-input placeholder="请输入API名称" v-model="form.name" />
			</el-col>

			<el-col :span="6">
				<h3>地址</h3>
				<el-input placeholder="请输入URL" v-model="form.url">
					<template #prepend>
						<el-select placeholder="选择" style="width: 100px" v-model="form.protol">
							<el-option label="http://" value="http" />
							<el-option label="https://" value="https" />
						</el-select>
					</template>
				</el-input>
			</el-col>

			<el-col :span="2">
				<h3>请求类型</h3>
				<el-select placeholder="选择" v-model="form.method">
					<el-option label="GET" value="get" />
					<el-option label="POST" value="post" />
				</el-select>
			</el-col>
		</el-row>

		<el-row :gutter="40">
			<el-col :span="4">
				<h3>请求头Head</h3>
			</el-col>

			<el-col :span="4">
				<h3>请求体Request</h3>
			</el-col>

			<el-col :span="4">
				<h3>返回体Response</h3>
			</el-col>
		</el-row>

    <el-button type="primary" @click="onSubmit">注册</el-button>
    <el-button>取消</el-button>
	</el-form>
</template>

<style scoped>
h2 {
	margin: 0;
}

span {
	margin: 10px 0;
}
</style>
