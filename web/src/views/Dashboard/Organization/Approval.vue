<script setup lang="ts">
import moment from 'moment'
import { ElNotification } from 'element-plus'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { organizationService } from '@/service/organization'
import { RegisterStatus, RegisterStatusDict, OrganizationType } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let approvalList: any[]
let showList = ref(Array())

onMounted(() => {
	loadOrganizationRegisterRequestList()
})

const loadOrganizationRegisterRequestList = () => {
	organizationService
		.organizationRegsiterRequestList()
		.then((res: any) => {
			console.log(res)
			approvalList = res.data
			total.value = approvalList.length
			showList.value = approvalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
			ElNotification({
				title: '未知错误',
				message: err.response.data,
				type: 'error',
			})
		})
}

const registerStatusToText = (item: string) => {
	switch (RegisterStatus[item]) {
		case RegisterStatus.PROCESSED:
			return 'text-[var(--el-color-warning)]'
		case RegisterStatus.ALLOW:
			return 'text-[var(--el-color-success)]'
		case RegisterStatus.REJECT:
			return 'text-[var(--el-color-danger)]'
		default:
			break
	}
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = approvalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
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
	organizationService
		.organizationRegisterRequsetReply(
			showList.value[selectedIndex.value].serialNumber,
			RegisterStatusDict[RegisterStatus.ALLOW]
		)
		.then((res: any) => {
			if (res.data) {
				allowDialogVisible.value = false
				loadOrganizationRegisterRequestList()
			}
		})
		.catch((err: any) => {
			ElNotification({
				title: '未知错误',
				message: err.response.data,
				type: 'error',
			})
		})
}

const rejectReason = ref('')
const rejectDialogVisible = ref(false)
const rejectConfirm = (index: number) => {
	rejectDialogVisible.value = true
	selectedIndex.value = index
}
const reject = () => {
	organizationService
		.organizationRegisterRequsetReply(
			showList.value[selectedIndex.value].serialNumber,
			RegisterStatusDict[RegisterStatus.REJECT],
			rejectReason.value
		)
		.then((res: any) => {
			if (res.data) {
				rejectDialogVisible.value = false
				loadOrganizationRegisterRequestList()
			}
		})
		.catch((err: any) => {
			ElNotification({
				title: '未知错误',
				message: err.response.data,
				type: 'error',
			})
		})
}
</script>

<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">机构注册审批</h2>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="60" />
				<el-table-column label="申请号">
					<template #default="scope">
						<CopyText :text="scope.row.serialNumber" />
					</template>
				</el-table-column>
				<el-table-column label="机构名称" prop="name" />
				<el-table-column label="机构类型">
					<template #default="scope">
						{{ OrganizationType[scope.row.type] }}
					</template>
				</el-table-column>
				<el-table-column label="状态">
					<template #default="scope">
						<span :class="registerStatusToText(scope.row.status)">
							{{ RegisterStatus[scope.row.status] }}
						</span>
					</template>
				</el-table-column>
				<el-table-column label="申请时间">
					<template #default="scope">
						{{ moment(scope.row.applyTime).format('YYYY-MM-DD HH:mm:ss') }}
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center operate">
							<el-tooltip content="详情">
								<el-button type="primary" icon="More" circle size="default" />
							</el-tooltip>
							<el-tooltip
								content="允许"
								v-if="(RegisterStatus[scope.row.status] as RegisterStatus) === RegisterStatus.PROCESSED"
							>
								<el-button type="success" icon="Check" circle size="default" @click="allowConfirm(scope.$index)" />
							</el-tooltip>
							<el-tooltip
								content="驳回"
								v-if="(RegisterStatus[scope.row.status] as RegisterStatus) === RegisterStatus.PROCESSED"
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
</template>

<style lang="less" scoped></style>
