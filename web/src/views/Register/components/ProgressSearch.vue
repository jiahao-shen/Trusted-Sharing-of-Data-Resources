<script setup lang="ts">
import { ref } from 'vue'
import { Icon } from '@iconify/vue'
import { EnumValues } from 'enum-values'
import { ElNotification } from 'element-plus'
import { CopyText } from '@/components/CopyText'
import { useRoute, useRouter } from 'vue-router'
import { organizationService } from '@/service/organization'
import { ApplyStatusText } from '@/components/ApplyStatusText'
import { UserRegisterDialog } from '@/components/UserRegisterDialog'
import { OrganizationType, RegisterStatus, UserType } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const search = ref('')
const pageSize = 10
const total = ref(0)
let currentPage = 1
let registerList: any[]
let showList = ref(Array())

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = registerList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}
const searchRegisterProgress = () => {
	let tmp = search.value.split(',')
	for (let item of tmp) {
		if (item.trim() === '' || !/^[0-9]+$/.test(item)) {
			ElNotification({
				title: '申请号格式错误',
				type: 'error',
			})
			break
		}
	}

	organizationService
		.organizationRegisterApplyProgress(tmp)
		.then((res: any) => {
			console.log(res.data)
			registerList = res.data
			total.value = registerList.length
			showList.value = registerList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
			ElNotification({
				title: '查询失败',
				message: err.response.data,
				type: 'error',
			})
		})
}

const selectedOrganization = ref('')
const userRegisterDialogVisible = ref(false)
const openRegisterAdminDialog = (item: any) => {
	selectedOrganization.value = item.id
	userRegisterDialogVisible.value = true
}

const closeRegisterAdminDialog = () => {
	userRegisterDialogVisible.value = false
}

const registerAdminSuccess = () => {
	userRegisterDialogVisible.value = false
}
</script>

<template>
	<div class="w-full h-full flex flex-col items-center justify-center">
		<h2 class="text-4xl">注册进度查询</h2>
		<div class="w-600px mt-40px">
			<el-input size="large" placeholder="请输入申请号" v-model="search">
				<template #append>
					<el-button icon="Search" @click="searchRegisterProgress"></el-button>
				</template>
			</el-input>
		</div>
		<div class="w-[80%] mt-40px">
			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="60" />
				<el-table-column label="申请号">
					<template #default="scope">
						<CopyText :text="scope.row.serialNumber" />
					</template>
				</el-table-column>
				<el-table-column label="机构名称" prop="name" />
				<el-table-column label="机构类型" width="100">
					<template #default="scope">
						{{ OrganizationType[scope.row.type] }}
					</template>
				</el-table-column>
				<el-table-column label="状态" width="100">
					<template #default="scope">
						<ApplyStatusText :status="scope.row.status" />
					</template>
				</el-table-column>
				<el-table-column label="申请时间" prop="applyTime" />
				<el-table-column label="操作" width="150">
					<template #default="scope">
						<div class="w-full h-full flex items-center">
							<el-tooltip content="详情">
								<el-button type="info" icon="More" circle size="default" />
							</el-tooltip>

							<el-tooltip
								content="创建管理员"
								v-if="(RegisterStatus[scope.row.status] as RegisterStatus) === RegisterStatus.ALLOW"
							>
								<el-button type="success" circle size="default" @click="openRegisterAdminDialog(scope.row)">
									<Icon icon="ant-design:user-add-outlined" />
								</el-button>
							</el-tooltip>
						</div>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<div class="w-full mt-20px flex items-center justify-center">
			<el-pagination
				layout="jumper, prev, pager, next,total"
				:total="total"
				:page-size="pageSize"
				@current-change="handleCurrentChange"
			/>
		</div>

		<el-button class="w-200px mt-40px" type="primary" @click="router.push('/login')">返回</el-button>

		<UserRegisterDialog
			title="注册管理员用户"
			:visible="userRegisterDialogVisible"
			:organization="selectedOrganization"
			:type="EnumValues.getNameFromValue(UserType, UserType.ADMIN)"
			@close="closeRegisterAdminDialog"
			@success="registerAdminSuccess"
		/>
	</div>
</template>

<style lang="less" scoped></style>
