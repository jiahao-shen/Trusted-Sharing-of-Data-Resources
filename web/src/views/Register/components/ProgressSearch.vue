<script setup lang="ts">
import { ref } from 'vue'
import moment from 'moment'
import { ElNotification } from 'element-plus'
import { OrganizationType } from '@/utils/enums'
import { CopyText } from '@/components/CopyText'
import { organizationService } from '@/service/organization'
import { ApplyStatusText } from '@/components/ApplyStatusText'

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
		if (item.trim() === '' || !(/^[0-9]+$/.test(item))) {
			ElNotification({
				title: '申请号格式错误',
				type: 'error',
			})
			break;
		}
	}

	organizationService
		.searchRegisterProgress(tmp)
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
</script>

<template>
	<div class="w-full h-full flex flex-col items-center justify-center">
		<div class="w-600px">
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
	</div>
</template>

<style lang="less" scoped></style>
