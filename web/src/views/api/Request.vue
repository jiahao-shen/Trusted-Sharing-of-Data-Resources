<script setup lang="ts">
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

<style></style>
