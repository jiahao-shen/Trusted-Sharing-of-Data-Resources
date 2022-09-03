<script lang="ts" setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Search, RefreshLeft } from '@element-plus/icons-vue'
import { CopyText } from '@/components/CopyText'
import { service } from '@/api/dashboard/api/request'

const route = useRoute()
const router = useRouter()

const classList = ref(new Set())
const functionList = ref(new Set())
const statusList = ref(new Set())

const orgClass = ref('全部')
const orgName = ref('')
const apiName = ref('')
const apiFunction = ref('全部')
const apiStatus = ref('全部')

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiList: any[]
let filterList: any[]
let showList = ref(Array())

service.getAPIList().then((res: any) => {
	if (res.status === 200) {
		apiList = res.data

		filterList = apiList
		total.value = filterList.length
		showList.value = filterList.slice((currentPage - 1) * pageSize, currentPage * pageSize)

		for (let item of apiList) {
			classList.value.add(item.orgClass)
			functionList.value.add(item.apiFunction)
			statusList.value.add(item.apiStatus)
		}
	}
})

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = filterList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const orgClassChange = (value: string) => {
	computeFilter()
}

const searchByOrgName = () => {
	computeFilter()
}

const searchByAPIID = () => {
	computeFilter()
}

const apiFunctionChange = (value: string) => {
	computeFilter()
}

const apiStatusChange = (value: string) => {
	computeFilter()
}

const resetFilter = () => {
	orgClass.value = '全部'
	orgName.value = ''
	apiName.value = ''
	apiFunction.value = '全部'
	apiStatus.value = '全部'

	computeFilter()
}

const computeFilter = () => {
	if (orgClass.value === '全部') {
		console.log('机构类别:全部')
		filterList = apiList
	} else {
		console.log('机构类别:筛选')
		filterList = apiList.filter((item) => item.orgClass === orgClass.value)
	}

	if (orgName.value.trim() !== '') {
		console.log('机构名称')
		console.log(filterList)
		filterList = filterList.filter((item) => item.orgName.includes(orgName.value))
	}

	if (apiName.value.trim() !== '') {
		console.log('API名称')
		filterList = filterList.filter((item) => item.apiName.includes(apiName.value))
	}

	if (apiFunction.value !== '全部') {
		console.log('API功能')
		filterList = filterList.filter((item) => item.apiFunction === apiFunction.value)
	}

	if (apiStatus.value !== '全部') {
		console.log('API状态')
		filterList = filterList.filter((item) => item.apiStatus === apiStatus.value)
	}

	total.value = filterList.length
	showList.value = filterList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const showDetails = () => {}

const requestAPI = (apiID: any) => {
	router.push({ name: '申请表单', query: { apiID: apiID } })
}
</script>

<template>
	<div class="w-full p-20px" v-if="route.name === 'API申请'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">平台API</span>
			</template>

			<div class="flex">
				<span class="inline-flex items-center">机构类别:</span>
				<el-radio-group class="mx-20px" v-model="orgClass" size="large" @change="orgClassChange">
					<el-radio-button label="全部" />
					<el-radio-button v-for="item in classList" :label="item" />
				</el-radio-group>
			</div>

			<div class="flex mt-20px">
				<span class="inline-flex items-center">机构名称:</span>
				<div class="w-400px mx-20px inline-flex items-center">
					<el-input size="large" v-model="orgName" placeholder="搜索" clearable>
						<template #append>
							<el-button :icon="Search" @click="searchByOrgName" />
						</template>
					</el-input>
				</div>
			</div>

			<el-row class="mt-20px">
				<el-col class="flex" :span="8">
					<span class="inline-flex items-center">API名称:</span>
					<div class="w-400px mx-20px">
						<el-input size="large" v-model="apiName" placeholder="搜索" clearable>
							<template #append>
								<el-button :icon="Search" @click="searchByAPIID" />
							</template>
						</el-input>
					</div>
				</el-col>

				<el-col class="flex" :span="6">
					<span class="inline-flex items-center">API功能:</span>
					<el-select class="mx-20px" size="large" v-model="apiFunction" @change="apiFunctionChange">
						<el-option label="全部" value="全部" />
						<el-option v-for="item in functionList" :label="item" :value="item" />
					</el-select>
				</el-col>

				<el-col class="flex" :span="6">
					<span class="inline-flex items-center">API状态:</span>
					<el-select class="mx-20px" size="large" v-model="apiStatus" @change="apiStatusChange">
						<el-option label="全部" value="全部" />
						<el-option v-for="item in statusList" :label="item" :value="item" />
					</el-select>
				</el-col>

				<el-col :span="3">
					<span class="inline-flex items-center">全部重置:</span>
					<el-button class="mx-20px" :icon="RefreshLeft" @click="resetFilter" />
				</el-col>
			</el-row>

			<el-table class="mt-20px" :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="机构" prop="orgName" />
				<el-table-column label="机构类别" prop="orgClass" />
				<el-table-column label="API名称" prop="apiName" />
				<el-table-column label="ID" prop="apiID" width="400">
					<template #default="scope">
						<CopyText :text="scope.row.apiID" />
					</template>
				</el-table-column>
				<el-table-column label="功能类型" prop="apiFunction" />
				<el-table-column label="状态" prop="apiStatus" />

				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center operate">
							<el-button type="primary" text>详情</el-button>
							<el-button type="primary" text @click="requestAPI(scope.row.apiID)">申请</el-button>
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
	</div>

	<router-view v-else />
</template>

<style lang="less" scoped>
.operate {
	.el-button {
		padding: 0px;
	}
}
</style>
