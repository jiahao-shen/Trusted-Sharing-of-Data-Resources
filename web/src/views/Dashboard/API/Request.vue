<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { OrganizationType, HttpMethod } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const apiCategoryList = ref(new Set())

const selectOrgType = ref('全部')
const queryOrgName = ref('')
const queryAPIName = ref('')
const selectAPICategory = ref('全部')
const selectAPIMethod = ref('全部')

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiList: any[]
let filterList: any[]
let showList = ref(Array())

onMounted(() => {
	loadAllAPIList()
})

const loadAllAPIList = () => {
	apiService
		.allAPIList()
		.then((res: any) => {
			apiList = res.data

			for (let item of apiList) {
				apiCategoryList.value.add(item.category)
			}

			filterList = apiList
			total.value = filterList.length
			showList.value = filterList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {})
}

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
	selectOrgType.value = '全部'
	queryOrgName.value = ''
	queryAPIName.value = ''
	selectAPICategory.value = '全部'
	selectAPIMethod.value = '全部'

	computeFilter()
}

const computeFilter = () => {
	if (selectOrgType.value === '全部') {
		console.log('机构类别:全部')
		filterList = apiList
	} else {
		console.log('机构类别:筛选')
		filterList = apiList.filter((item) => item.organizationType === selectOrgType.value)
	}

	if (queryOrgName.value.trim() !== '') {
		console.log('机构名称')
		filterList = filterList.filter((item) => item.organizationName.includes(queryOrgName.value))
	}

	if (queryAPIName.value.trim() !== '') {
		console.log('API名称')
		filterList = filterList.filter((item) => item.name.includes(queryAPIName.value))
	}

	if (selectAPICategory.value !== '全部') {
		console.log('API功能')
		filterList = filterList.filter((item) => item.category === selectAPICategory.value)
	}

	if (selectAPIMethod.value !== '全部') {
		console.log('请求类型')
		filterList = filterList.filter((item) => item.method === selectAPIMethod.value)
	}

	total.value = filterList.length
	showList.value = filterList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const showDetails = () => {}
</script>

<template>
	<div class="w-full p-20px" v-if="route.name === 'API申请'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">{{ route.name }}</span>
			</template>

			<div class="flex">
				<span class="inline-flex items-center">机构类别:</span>
				<el-radio-group class="mx-20px" v-model="selectOrgType" @change="orgClassChange">
					<el-radio-button label="全部" />
					<el-radio-button
						v-for="item in OrganizationType"
						:label="EnumValues.getNameFromValue(OrganizationType, item)"
					>
						{{ item }}
					</el-radio-button>
				</el-radio-group>
			</div>

			<div class="flex mt-20px">
				<span class="inline-flex items-center">机构名称:</span>
				<div class="w-400px mx-20px inline-flex items-center">
					<el-input v-model="queryOrgName" placeholder="搜索" clearable>
						<template #append>
							<el-button icon="Search" @click="searchByOrgName" />
						</template>
					</el-input>
				</div>
			</div>

			<el-row class="mt-20px">
				<el-col class="flex" :span="8">
					<span class="inline-flex items-center">API名称:</span>
					<div class="w-400px mx-20px">
						<el-input v-model="queryAPIName" placeholder="搜索" clearable>
							<template #append>
								<el-button icon="Search" @click="searchByAPIID" />
							</template>
						</el-input>
					</div>
				</el-col>

				<el-col class="flex" :span="6">
					<span class="inline-flex items-center">API功能:</span>
					<el-select class="mx-20px" v-model="selectAPICategory" @change="apiFunctionChange">
						<el-option label="全部" value="全部" />
						<!-- TODO: -->
						<el-option v-for="item in apiCategoryList" :label="item" :value="item" />
					</el-select>
				</el-col>

				<el-col class="flex" :span="6">
					<span class="inline-flex items-center">请求类型:</span>
					<el-select class="mx-20px" v-model="selectAPIMethod" @change="apiStatusChange">
						<el-option label="全部" value="全部" />
						<el-option
							v-for="item in HttpMethod"
							:label="item"
							:value="EnumValues.getNameFromValue(HttpMethod, item)"
						/>
					</el-select>
				</el-col>

				<el-col :span="3">
					<span class="inline-flex items-center">全部重置:</span>
					<el-button class="mx-20px" icon="RefreshLeft" @click="resetFilter" />
				</el-col>
			</el-row>

			<el-table class="mt-20px" :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="API ID">
					<template #default="scope">
						<CopyText :text="scope.row.id" />
					</template>
				</el-table-column>
				<el-table-column label="API名称" prop="name" />
				<el-table-column label="请求类型">
					<template #default="scope">
						<el-tag type="success">{{ scope.row.method }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="功能分类" prop="category" />
				<el-table-column label="所属机构" prop="organizationName" />
				<el-table-column label="机构类别">
					<template #default="scope">
						<el-tag>{{ OrganizationType[scope.row.organizationType] }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createdTime" />

				<el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center">
							<el-tooltip content="详情">
								<el-button type="primary" icon="More" circle size="default" />
							</el-tooltip>
							<el-tooltip content="申请调用">
								<el-button
									type="success"
									icon="DocumentAdd"
									circle
									size="default"
									@click="
										router.push({
											name: '申请表单',
											query: { orgName: scope.row.organizationName, apiName: scope.row.name, apiID: scope.row.id },
										})
									"
								/>
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
		</el-card>
	</div>

	<router-view v-else />
</template>

<style lang="less" scoped></style>
