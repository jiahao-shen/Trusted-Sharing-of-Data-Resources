<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { RegisterStatus } from '@/utils/enums'
import { CopyText } from '@/components/CopyText'
import { useRouter, useRoute } from 'vue-router'
import { ApplyStatusText } from '@/components/ApplyStatusText'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiRegisterApplyList: any[]
let showList = ref(Array())

onMounted(() => {
	loadAPIRegisterApplyList()
})

const loadAPIRegisterApplyList = () => {
	apiService
		.apiRegisterApplyList()
		.then((res: any) => {
			console.log(res.data)
			apiRegisterApplyList = res.data
			total.value = apiRegisterApplyList.length
			showList.value = apiRegisterApplyList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch()
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = apiRegisterApplyList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const selectedIndex = ref()
const allowDialogVisible = ref(false)
const allowConfirm = (index: number) => {
	allowDialogVisible.value = true
	selectedIndex.value = index
}
const allow = () => {
	apiService
		.apiRegisterReply(
			showList.value[selectedIndex.value].serialNumber,
			EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.ALLOW)
		)
		.then((res: any) => {
			if (res.data) {
				allowDialogVisible.value = false
				loadAPIRegisterApplyList()
			}
		})
		.catch((err: any) => {
		})
}

const rejectReason = ref('')
const rejectDialogVisible = ref(false)
const rejectConfirm = (index: number) => {
	rejectDialogVisible.value = true
	selectedIndex.value = index
}
const reject = () => {
	apiService
		.apiRegisterReply(
			showList.value[selectedIndex.value].serialNumber,
			EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.REJECT),
			rejectReason.value
		)
		.then((res: any) => {
			if (res.data) {
				rejectDialogVisible.value = false
				loadAPIRegisterApplyList()
			}
		})
		.catch((err: any) => {
		})
}
</script>

<template>
	<div class="w-full p-20px" v-if="route.name === 'API注册'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">{{ route.name }}</span>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="申请号" width="400">
					<template #default="scope">
						<CopyText :text="scope.row.serialNumber" />
					</template>
				</el-table-column>
				<el-table-column label="名称" prop="name" />
				<el-table-column label="状态">
					<template #default="scope">
						<ApplyStatusText :status="scope.row.status" />
					</template>
				</el-table-column>
				<el-table-column label="申请时间" prop="applyTime" />
				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center">
							<el-tooltip content="详情">
								<el-button type="primary" icon="More" circle size="default" />
							</el-tooltip>
							<el-tooltip
								content="通过"
								v-if="RegisterStatus[scope.row.status] === RegisterStatus.PROCESSED"
							>
								<el-button type="success" icon="Check" circle size="default" @click="allowConfirm(scope.$index)" />
							</el-tooltip>
							<el-tooltip
								content="驳回"
								v-if="RegisterStatus[scope.row.status] === RegisterStatus.PROCESSED"
							>
								<el-button type="danger" icon="Close" circle size="default" @click="rejectConfirm(scope.$index)" />
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

			<el-button class="w-full mt-20px" plain @click="router.push('/dashboard/api/register/form')">
				<el-icon><Plus /></el-icon>
				&nbsp;新增API
			</el-button>
		</el-card>

		<el-dialog v-model="allowDialogVisible" title="提示" width="20%">
			<h2 class="text-base">允许 {{ showList[selectedIndex].name }} 的注册申请?</h2>
			<template #footer>
				<el-button @click="allowDialogVisible = false">取消</el-button>
				<el-button type="primary" @click="allow">确认</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="rejectDialogVisible" title="提示" width="20%">
			<h2 class="text-base">驳回 {{ showList[selectedIndex].name }}的注册申请?</h2>
			<el-input
				class="mt-20px"
				:rows="5"
				type="textarea"
				maxlength="200"
				show-word-limit
				placeholder="请填写理由"
				v-model="rejectReason"
			/>
			<template #footer>
				<el-button @click="rejectDialogVisible = false">取消</el-button>
				<el-button type="primary" @click="reject">确认</el-button>
			</template>
		</el-dialog>
	</div>
	<router-view v-else />
</template>

<style lang="less" scoped></style>
