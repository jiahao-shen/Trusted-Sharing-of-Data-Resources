<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { OrganizationType, HttpMethod } from '@/utils/enums'

const emit = defineEmits(['select'])

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
	<div>
		<el-row class="">
			<el-col>
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
			</el-col>
		</el-row>

		<el-row class="mt-20px">
			<el-col>
				<span class="inline-flex items-center">机构名称:</span>
				<div class="w-400px mx-20px inline-flex items-center">
					<el-input v-model="queryOrgName" placeholder="搜索" clearable>
						<template #append>
							<el-button icon="Search" @click="searchByOrgName" />
						</template>
					</el-input>
				</div>
			</el-col>
		</el-row>

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
					<el-option v-for="item in apiCategoryList" :label="item" :value="item" />
				</el-select>
			</el-col>

			<el-col class="flex" :span="6">
				<span class="inline-flex items-center">请求类型:</span>
				<el-select class="mx-20px" v-model="selectAPIMethod" @change="apiStatusChange">
					<el-option label="全部" value="全部" />
					<el-option v-for="item in HttpMethod" :label="item" :value="EnumValues.getNameFromValue(HttpMethod, item)" />
				</el-select>
			</el-col>

			<el-col :span="3">
				<span class="inline-flex items-center">全部重置:</span>
				<el-button class="mx-20px" icon="RefreshLeft" @click="resetFilter" />
			</el-col>
		</el-row>

		<el-table class="mt-20px" :data="showList" highlight-current-row border>
			<el-table-column label="No." type="index" :index="indexMethod" width="60" />
			<el-table-column label="API ID" width="250">
				<template #default="scope">
					<CopyText :text="scope.row.id" />
				</template>
			</el-table-column>
			<el-table-column label="API名称" prop="name" />
			<el-table-column label="请求类型" prop="method" width="100" />
			<el-table-column label="功能分类" prop="category" width="100" />
			<el-table-column label="所属机构" prop="organizationName" />
			<el-table-column label="机构类别" width="100">
				<template #default="scope">
					<el-tag>{{ OrganizationType[scope.row.organizationType] }}</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="创建时间" prop="createdTime" />

			<el-table-column label="操作" width="150">
				<template #default="scope">
					<div class="w-full h-full flex items-center">
						<el-tooltip content="详情">
							<el-button type="primary" icon="More" circle size="default" />
						</el-tooltip>
						<el-tooltip content="申请调用">
							<el-button type="success" icon="DocumentAdd" circle size="default" @click="emit('select', scope.row)" />
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
	</div>
</template>

<style lang="less" scoped></style>
