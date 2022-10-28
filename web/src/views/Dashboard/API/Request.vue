<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { APISelection } from '@/components/APISelection'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let apiInvokeRequestList: any[]
let showList = ref(Array())

onMounted(() => {
	loadAPIInvokeRequestList()
})

const loadAPIInvokeRequestList = () => {
	// apiService.
}


const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = apiInvokeRequestList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
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
</script>

<template>
	<div class="w-full p-20px" v-if="route.name === 'API调用'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">{{ route.name }}</span>
			</template>

			<el-table> 

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
	</div>

	<router-view v-else />
</template>

<style lang="less" scoped></style>
