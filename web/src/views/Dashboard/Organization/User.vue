<script setup lang="ts">
import { ref } from 'vue'
import moment from 'moment'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let userList: any[]
let showList = ref(Array())

const init = () => {
	userList = [
		{
			username: 'admin',
			password: 'admin',
			permission: ['权限A', '权限B'],
			createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
		},
	]

	total.value = userList.length
	showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

init()
const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}
</script>
<template>
	<div class="w-full p-20px" v-if="route.name === '用户管理'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">用户管理</span>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="用户名" prop="username" />
				<el-table-column label="权限" prop="permissions">
					<template #default="scope">
					<el-tag class="mx-5px" v-for="item in scope.row.permission">{{ item }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createdTime" />
				<el-table-column label="操作">
					<template #default scope>
						<div class="w-full h-full flex items-center operate">
							<el-button type="success" icon="Edit" circle size="default" />
							<el-button type="danger" icon="Delete" circle size="default" />
						</div>
					</template>
				</el-table-column>
			</el-table>

			<div class="w-full mt-20px flex items-center justify-center">
				<el-pagination
					layout="jumper, prev, pager, next,total"
					:total="total"
					:page-size="pageSize"
					@current-change="handleCurrentChange"
				/>
			</div>

			<el-button class="w-full mt-20px" plain @click="router.push('')">
				<el-icon><Plus /></el-icon>
				&nbsp新增用户
			</el-button>
		</el-card>
	</div>

	<router-view v-else />
</template>
<style scoped></style>
