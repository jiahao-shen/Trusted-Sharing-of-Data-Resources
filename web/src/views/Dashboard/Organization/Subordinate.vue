<script setup lang="ts">
import { EnumValues } from 'enum-values'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { CopyText } from '@/components/CopyText'
import { organizationService } from '@/service/organization'
import { ApplyStatusText } from '@/components/ApplyStatusText'
import { RegisterStatus, OrganizationType } from '@/utils/enums'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let subordinateList: any[]
let showList = ref(Array())

onMounted(() => {
	loadOrganizationSubordinateList()
})

const loadOrganizationSubordinateList = () => {
	organizationService
		.organizationSubordinateList()
		.then((res: any) => {
			console.log(res.data)
			subordinateList = res.data
			total.value = subordinateList.length
			showList.value = subordinateList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
		})
		.catch((err: any) => {
		})
}

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = subordinateList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
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
				<el-table-column label="机构ID">
					<template #default="scope">
						<CopyText :text="scope.row.id" />
					</template>
				</el-table-column>
				<el-table-column label="机构名称" prop="name" />
				<el-table-column label="机构类型">
					<template #default="scope">
						<el-tag>{{ OrganizationType[scope.row.type] }}</el-tag>
					</template>
				</el-table-column>
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
