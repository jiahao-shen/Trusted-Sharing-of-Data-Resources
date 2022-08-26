<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import useClipboard from 'vue-clipboard3'
import { ElMessage } from 'element-plus'
import { service } from '@/api/dashboard/api/request'

const route = useRoute()
const { toClipboard } = useClipboard()

const classList = ref(new Set())
const functionList = ref(new Set())
const statusList = ref(new Set())

const orgClass = ref('全部')
const orgName = ref('')
const apiName = ref('')
const apiFunction = ref('')
const apiStatus = ref('')

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiList: any[]
let showList = ref(Array())

service.getAPIList().then((res: any) => {
	if (res.status === 200) {
		apiList = res.data
		total.value = apiList.length
		showList.value = apiList.slice((currentPage - 1) * pageSize, currentPage * pageSize)

		for (let item of apiList) {
			classList.value.add(item.orgClass)
			functionList.value.add(item.apiFunction)
			statusList.value.add(item.apiStatus)
		}
	}
})

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = apiList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

const copyID = async (text: string) => {
	try {
		await toClipboard(text)
		ElMessage('复制成功')
	} catch (err) {
		console.error(err)
	}
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
				<el-radio-group class="mx-20px" v-model="orgClass" size="large">
					<el-radio-button label="全部" />
					<el-radio-button v-for="item in classList" :label="item" />
				</el-radio-group>
			</div>

			<div class="flex mt-20px">
				<span class="inline-flex items-center">机构名称:</span>
				<div class="w-400px mx-20px inline-flex items-center">
					<el-input size="large" v-model="orgName" placeholder="搜索" clearable>
						<template #append>
							<el-button :icon="Search" />
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
								<el-button :icon="Search" />
							</template>
						</el-input>
					</div>
				</el-col>

				<el-col class="flex" :span="6">
					<span class="inline-flex items-center">API功能:</span>
					<el-select class="mx-20px" size="large" v-model="apiFunction">
						<el-option v-for="item in functionList" :label="item" :value="item" />
					</el-select>
				</el-col>

				<el-col class="flex" :span="6">
					<span class="inline-flex items-center">API状态:</span>
					<el-select class="mx-20px" size="large" v-model="apiStatus">
						<el-option v-for="item in statusList" :label="item" :value="item" />
					</el-select>
				</el-col>
			</el-row>

			<el-table class="mt-20px" :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="机构" prop="orgName" />
				<el-table-column label="API名称" prop="apiName" />
				<el-table-column label="ID" prop="apiID" width="400">
					<template #default="scope">
						<div class="cursor-pointer" @click="copyID(scope.row.apiID)">
							<span>{{ scope.row.apiID }}</span>
							<el-icon><CopyDocument /></el-icon>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="功能类型" prop="apiFunction" />
				<el-table-column label="状态" prop="apiStatus" />

				<el-table-column label="操作">
					<template #default scope>
						<div class="w-full h-full flex items-center operate">
							<el-button type="primary" text>详情</el-button>
							<el-button type="primary" text>申请</el-button>
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

<style scoped></style>
