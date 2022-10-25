<script setup lang="ts">
import { useAppStore } from '@/store/app'
import { userService } from '@/service/user'
import { ElNotification } from 'element-plus'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { UserRegisterDialog } from '@/components/UserRegisterDialog'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let userList: any[]
let showList = ref(Array())

const userRegisterDialogVisible = ref(false)
const openUserRegisterDialog = (item: any) => {
	userRegisterDialogVisible.value = true
}

const closeUserRegisterDialog = () => {
	userRegisterDialogVisible.value = false
}

const userRegisterSuccess = () => {
	userRegisterDialogVisible.value = false
	loadUserList()
}

onMounted(() => {
	loadUserList()
})

const loadUserList = () => {
	userService
		.userList()
		.then((res: any) => {
			console.log(res.data)
			userList = res.data
			total.value = userList.length
			showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {})
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}
</script>
<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">用户管理</h2>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="账号" prop="id" />
				<el-table-column label="用户名" prop="username" />
				<el-table-column label="权限" prop="permissions">
					<template #default="scope">
						<el-tag class="mx-5px" v-for="item in scope.row.permission">{{ item }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createdTime" />
				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center operate">
							<el-tooltip content="编辑">
								<el-button type="success" icon="Edit" circle size="default" />
							</el-tooltip>
							<el-tooltip content="删除">
								<el-button type="danger" icon="Delete" circle size="default" />
							</el-tooltip>
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

			<el-button class="w-full mt-20px" plain @click="openUserRegisterDialog">
				<el-icon><Plus /></el-icon>
				&nbsp新增用户
			</el-button>
		</el-card>
	</div>

	<UserRegisterDialog
		title="注册新账户"
		:visible="userRegisterDialogVisible"
		:organization="appStore.getUser.organization"
		@close="closeUserRegisterDialog"
		@success="userRegisterSuccess"
	/>
</template>
<style lang="less" scoped>
#form {
	:deep(.el-form-item__label) {
		font-size: 1rem;
	}
}
</style>
