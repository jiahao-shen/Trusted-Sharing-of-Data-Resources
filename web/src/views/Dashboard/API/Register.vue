<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { RegisterStatus } from '@/utils/enums'
import { useRouter, useRoute } from 'vue-router'

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
		.catch((err: any) => {})
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = apiRegisterApplyList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const allow = (item: any) => {
	apiService
		.apiRegisterReply(item.serialNumber, EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.ALLOW))
		.then((res: any) => {
			if (res.data) {
				loadAPIRegisterApplyList()
			}
		})
		.catch((err: any) => {})
}

const reject = (item: any, reason: string) => {
	apiService
		.apiRegisterReply(item.serialNumber, EnumValues.getNameFromValue(RegisterStatus, RegisterStatus.REJECT), reason)
		.then((res: any) => {
			if (res.data) {
				loadAPIRegisterApplyList()
			}
		})
		.catch((err: any) => {})
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
				<el-table-column label="API名称" prop="name" />
				<el-table-column label="请求类型" prop="method" />
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
							<AllowButton
								v-if="RegisterStatus[scope.row.status] === RegisterStatus.PROCESSED"
								title="提示"
								:text="`允许 ${scope.row.name} 的注册申请?`"
								:item="scope.row"
								@allow="allow"
							/>
							<RejectButton
								v-if="RegisterStatus[scope.row.status] === RegisterStatus.PROCESSED"
								title="提示"
								:text="`驳回 ${scope.row.name} 的注册申请?`"
								:item="scope.row"
								@reject="reject"
							/>
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
	</div>
	<router-view v-else />
</template>

<style lang="less" scoped></style>
