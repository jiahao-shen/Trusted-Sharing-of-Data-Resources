<script setup lang="ts">
import { EnumValues } from 'enum-values'
import { apiService } from '@/service/api'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { HttpMethod } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let myAPIList: any[]
let showList = ref(Array())

onMounted(() => {
  loadMyAPIList()
})

const loadMyAPIList = () => {
	apiService
		.myAPIList()
		.then((res: any) => {
			console.log(res.data)
			myAPIList = res.data
			total.value = myAPIList.length
			showList.value = myAPIList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
		})
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = myAPIList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

</script>
<template>
  <div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">{{ route.name }}</h2>
			</template>

			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="60" />
				<el-table-column label="API ID">
					<template #default="scope">
						<CopyText :text="scope.row.id" />
					</template>
				</el-table-column>
				<el-table-column label="API名称" prop="name" />
				<el-table-column label="请求方法" prop="method" />
				<el-table-column label="版本" prop="version" />
				<el-table-column label="创建时间" prop="createdTime" />
        <el-table-column label="操作">
					<template #default="scope">
						<div class="w-full h-full flex items-center">
							<el-tooltip content="详情">
								<el-button type="primary" icon="More" circle size="default" />
							</el-tooltip>
              <el-tooltip content="编辑">
								<el-button type="success" icon="Edit" circle size="default" />
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

	</div>
</template>
<style scoped></style>
