<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { APIInvokeMethod } from '@/utils/enums'
import { useRoute, useRouter } from 'vue-router'
import { RegisterStatus, OrganizationType } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiInvokeApprovalList: any[]
let showList = ref(Array())

onMounted(() => {
	loadAPIInvokeApprovalList()
})

const loadAPIInvokeApprovalList = () => {
	apiService
		.apiInvokeApprovalList()
		.then((res: any) => {
			console.log(res.data)
			apiInvokeApprovalList = res.data
			total.value = apiInvokeApprovalList.length
			showList.value = apiInvokeApprovalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
			console.error(err)
		})
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = apiInvokeApprovalList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const allow = (item: any) => {
	apiService
		.apiInvokeReply(item.serialNumber, EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.ALLOW))
		.then((res: any) => {
			if (res.data) {
				loadAPIInvokeApprovalList()
			}
		})
		.catch((err: any) => {})
}

const reject = (item: any, reason: string) => {
	apiService
		.apiInvokeReply(item.serialNumber, EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.REJECT), reason)
		.then((res: any) => {
			if (res.data) {
				loadAPIInvokeApprovalList()
			}
		})
		.catch((err: any) => {})
}
</script>

<template>
	<div class="w-full p-20px">
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
				<el-table-column label="申请人" prop="applicantName" />
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
							<AllowButton
								v-if="RegisterStatus[scope.row.status] === RegisterStatus.PROCESSED"
								title="提示"
								:text="`允许 ${scope.row.apiName} 的调用申请?`"
								:item="scope.row"
								@allow="allow"
							/>
							<RejectButton
								v-if="RegisterStatus[scope.row.status] === RegisterStatus.PROCESSED"
								title="提示"
								:text="`驳回 ${scope.row.apiName} 的调用申请?`"
								:item="scope.row"
								@reject="reject"
							/>
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
		</el-card>
	</div>
</template>

<style lang="less" scoped></style>
