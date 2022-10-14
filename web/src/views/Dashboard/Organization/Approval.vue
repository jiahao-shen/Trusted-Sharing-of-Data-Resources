<script setup lang="ts">
import moment from 'moment'
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let approvalList: any[]
let showList = ref(Array())

const init = () => {
	approvalList = [
		{
			serial: '252221765281',
			organization: '机构一',
			status: '待审批',
			createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
		},
		{
			serial: '217652825221',
			organization: '机构二',
			status: '审批通过',
			createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
		},
		{
			serial: '215282722165',
			organization: '机构三',
			status: '审批未通过',
			createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
		},
	]

	total.value = approvalList.length
	showList.value = approvalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

init()

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = approvalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const selectedOrganization = ref('')

const allowDialogVisible = ref(false)
const allow = (index: number) => {
	allowDialogVisible.value = true
	selectedOrganization.value = approvalList[index].organization
}

const rejectReason = ref('')
const rejectDialogVisible = ref(false)
const reject = (index: number) => {
	rejectDialogVisible.value = true
	selectedOrganization.value = approvalList[index].organization
}
</script>

<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">机构注册审批</h2>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="申请号" prop="serial" />
				<el-table-column label="机构名称" prop="organization" />
				<el-table-column label="状态" prop="status">
					<template #default="scope">
						<span class="text-[var(--el-color-warning)]" v-if="scope.row.status === '待审批'">{{
							scope.row.status
						}}</span>
						<span class="text-[var(--el-color-success)]" v-else-if="scope.row.status === '审批通过'">{{
							scope.row.status
						}}</span>
						<span class="text-[var(--el-color-danger)]" v-else-if="scope.row.status === '审批未通过'">{{
							scope.row.status
						}}</span>
					</template>
				</el-table-column>
				<el-table-column label="申请时间" prop="createdTime" />
				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center operate">
							<el-tooltip content="详情">
								<el-button type="primary" icon="More" circle size="default" />
							</el-tooltip>
							<el-tooltip content="允许" v-if="scope.row.status === '待审批'">
								<el-button type="success" icon="Check" circle size="default" @click="allow(scope.$index)" />
							</el-tooltip>
							<el-tooltip content="驳回" v-if="scope.row.status === '待审批'">
								<el-button type="danger" icon="Close" circle size="default" @click="reject(scope.$index)" />
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
			<h2 class="text-base">允许 {{ selectedOrganization }} 的注册申请?</h2>
			<template #footer>
				<el-button @click="allowDialogVisible = false">取消</el-button>
				<el-button type="primary" @click="allowDialogVisible = false">确认</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="rejectDialogVisible" title="提示" width="20%">
			<h2 class="text-base">驳回 {{ selectedOrganization }}的注册申请?</h2>
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
				<el-button type="primary" @click="rejectDialogVisible = false">确认</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<style lang="less" scoped></style>
