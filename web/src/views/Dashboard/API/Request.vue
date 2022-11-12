<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { useRoute, useRouter } from 'vue-router'
import { APISelection } from '@/components/APISelection'
import { APIInvokeMethod, RegisterStatus } from '@/utils/enums'
import { InvokeResponseDialog } from '@/components/InvokeResponseDialog'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiInvokeApplyList: any[]
let showList = ref(Array())

onMounted(() => {
	loadAPIInvokeRequestList()
})

const loadAPIInvokeRequestList = () => {
	apiService
		.apiInvokeApplyList()
		.then((res: any) => {
			console.log(res.data)
			apiInvokeApplyList = res.data
			total.value = apiInvokeApplyList.length
			showList.value = apiInvokeApplyList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
			console.error(err)
		})
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = apiInvokeApplyList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const apiSelectionVisible = ref(false)

const selectAPI = (api: any) => {
	console.log(api)
	router.push({
		name: '申请表单',
		query: { apiID: api.id, apiName: api.name, orgName: api.organizationName },
	})
}

const invokeResponseDialogVisible = ref(false)

const invokeResponse = ref('')

const invokeByWeb = (invokeAPI: any) => {
	console.log('Invoke')
	console.log(invokeAPI)

	apiService
		.apiInvokeByWeb(invokeAPI)
		.then((res: any) => {
			invokeResponse.value = res
			invokeResponseDialogVisible.value = true
		})
		.catch((err: any) => {
			console.error(err)
		})
}
</script>

<template>
	<div class="w-full p-20px" v-if="route.name === 'API调用'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">{{ route.name }}</span>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="80" />
				<el-table-column label="申请号" width="300">
					<template #default="scope">
						<CopyText :text="scope.row.serialNumber" />
					</template>
				</el-table-column>
				<el-table-column label="API名称" prop="apiName" />
				<el-table-column label="机构名称" prop="organizationName" />
				<el-table-column label="调用方式">
					<template #default="scope">
						<el-tag>{{ APIInvokeMethod[scope.row.invokeMethod] }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="申请状态">
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
								content="调用"
								v-if="
									APIInvokeMethod[scope.row.invokeMethod] === APIInvokeMethod.WEB &&
									RegisterStatus[scope.row.status] === RegisterStatus.ALLOW
								"
							>
								<el-button type="success" icon="Promotion" circle size="default" @click="invokeByWeb(scope.row)" />
							</el-tooltip>
						</div>
					</template>
				</el-table-column>
			</el-table>

			<div class="w-full mt-20px flex items-center justify-center">
				<el-pagination
					layout="jumper, prev, pager, next, total"
					:total="total"
					:page-size="pageSize"
					@current-change="handleCurrentChange"
				/>
			</div>

			<el-button class="w-full mt-20px" plain @click="apiSelectionVisible = true">
				<el-icon><Plus /></el-icon>
				&nbsp;申请调用
			</el-button>
		</el-card>

		<el-dialog v-model="apiSelectionVisible" title="API列表" width="90%">
			<APISelection @select="selectAPI" />
		</el-dialog>

		<InvokeResponseDialog
			:visible="invokeResponseDialogVisible"
			title="调用结果"
			:response="invokeResponse"
			@close="invokeResponseDialogVisible = false"
		/>
	</div>

	<router-view v-else />
</template>

<style lang="less" scoped></style>
