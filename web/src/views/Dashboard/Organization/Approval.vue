<script setup lang="ts">
import { EnumValues } from 'enum-values'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { organizationService } from '@/service/organization'
import { ApplyStatusText } from '@/components/ApplyStatusText'
import { RegisterStatus, OrganizationType } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let approvalList: any[]
let showList = ref(Array())

onMounted(() => {
	loadOrganizationRegisterApprovalList()
})

const loadOrganizationRegisterApprovalList = () => {
	organizationService
		.organizationRegsiterApprovalList()
		.then((res: any) => {
			console.log(res.data)
			approvalList = res.data
			total.value = approvalList.length
			showList.value = approvalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
		})
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
		.organizationRegisterReply(
			showList.value[selectedIndex.value].serialNumber,
			EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.ALLOW)
		)
		.then((res: any) => {
			if (res.data) {
				allowDialogVisible.value = false
				loadOrganizationRegisterApprovalList()
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
	organizationService
		.organizationRegisterReply(
			showList.value[selectedIndex.value].serialNumber,
			EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.REJECT),
			rejectReason.value
		)
		.then((res: any) => {
			if (res.data) {
				rejectDialogVisible.value = false
				loadOrganizationRegisterApprovalList()
			}
		})
		.catch((err: any) => {
		})
}
</script>

<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">{{ route.name }}</h2>
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
