<!-- <script setup lang="ts">
const formData = {
	inforData: [
		{
			name: '',
			sex: '男',
			age: '',
		},
		{
			name: '',
			sex: '男',
			age: '',
		},
		{
			name: '',
			sex: '男',
			age: '',
		},
		{
			name: '',
			sex: '男',
			age: '',
		},
	],

	inforRules: {
		name: [{ required: true, message: '请输入姓名' }],
		sex: [{ required: true, message: '请输入性别' }],
		age: [{ required: true, message: '请输入年龄' }],
	},
}
function rules() {}
</script>

<template>
	<el-form :model="formData" ref="formData" style="width: 500px; height: 200px;">
		<el-table :data="formData.inforData" tooltip-effect="dark" size="small">
			<el-table-column label="名称">
				<template #default="scope">
					<el-form-item :prop="'inforData.' + scope.$index + '.name'" :rules="formData.inforRules.name">
						<el-input v-model="scope.row.name"></el-input>
					</el-form-item>
				</template>
			</el-table-column>
			<el-table-column label="性别">
				<template #default="scope">
					<el-form-item :prop="'inforData.' + scope.$index + '.sex'" :rules="formData.inforRules.sex">
						<el-input v-model="scope.row.sex"></el-input>
					</el-form-item>
				</template>
			</el-table-column>
			<el-table-column label="年龄">
				<template #default="scope">
					<el-form-item :prop="'inforData.' + scope.$index + '.age'" :rules="formData.inforRules.age">
						<el-input v-model="scope.row.age"></el-input>
					</el-form-item>
				</template>
			</el-table-column>
		</el-table>
	</el-form>
	<el-button @click="rules">点击校验</el-button>
</template>

<style></style> -->
<template>
	<el-form :model="forms" ref="forms" :rules="rules">
		<el-table :data="forms.data">
			<el-table-column prop="fundRatio" label="当前占比（%）" width="119"></el-table-column>
			<el-table-column prop="fundRatio">
				<template #default="scope">
					<el-form-item :prop="'data.' + scope.$index + '.fundRatioA'" :rules="rules.numVerify">
						<el-input v-model="scope.row.fundRatioA"></el-input>
					</el-form-item>
				</template>
			</el-table-column>
		</el-table>
		<el-button type="primary" @click="">提交</el-button>
	</el-form>
</template>
<script setup lang="ts">
let forms = {
	data: [],
}
let rules = {
	numVerify: [{ required: true, message: '占比应在1-99之间', trigger: 'blur' }],
}
</script>
