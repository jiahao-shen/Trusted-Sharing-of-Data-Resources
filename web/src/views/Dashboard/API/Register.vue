<script setup lang="ts">
import { ref } from 'vue'
import { CopyText } from '@/components/CopyText'
import { useRouter, useRoute } from 'vue-router'
import { service } from '@/api/dashboard/api/register'

const route = useRoute()
const router = useRouter()

const pageSize = 10
const total = ref(0)
let currentPage = 1
let registerList: any[]
let showList = ref(Array())

service.getRegisterList().then((res: any) => {
	if (res.status === 200) {
		registerList = res.data
		total.value = registerList.length
		showList.value = registerList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
	}
})

const handleCurrentChange = (value: number) => {
	currentPage = value
	showList.value = registerList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
}

const indexMethod = (index: number) => {
	return (currentPage - 1) * pageSize + index + 1
}

</script>

<template>
	<div class="w-full p-20px" v-if="route.name === 'API注册'">
		<el-card class="w-full">
			<template #header>
				<span class="text-2xl">API注册</span>
			</template>

			<h2 class="text-xl">当前注册</h2>
			<el-table :data="showList" highlight-current-row border>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="名称" prop="name" />
				<el-table-column label="ID" prop="id" width="400">
					<template #default="scope">
						<CopyText :text="scope.row.id" />
					</template>
				</el-table-column>
				<el-table-column label="状态" prop="status">
					<template #default="scope">
						<span class="text-[var(--el-color-primary)]" v-if="scope.row.status === '正在审核'">{{
							scope.row.status
						}}</span>
						<span class="text-[var(--el-color-success)]" v-else-if="scope.row.status === '审核通过'">{{
							scope.row.status
						}}</span>
						<span class="text-[var(--el-color-danger)]" v-else-if="scope.row.status === '审核失败'">{{
							scope.row.status
						}}</span>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createdTime" />
				<el-table-column label="操作">
					<template #default scope>
						<div class="w-full h-full flex items-center operate">
							<el-button type="primary" text>编辑</el-button>
							<el-button type="primary" text>删除</el-button>
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

			<el-button class="w-full mt-20px" plain @click="router.push('/dashboard/api/register/form')" size="large">
				<el-icon><Plus /></el-icon>
				&nbsp新增API
			</el-button>
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
