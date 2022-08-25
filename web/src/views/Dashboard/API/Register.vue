<script setup lang="ts">
import { ref } from 'vue'
import { service } from '@/api/dashboard/api/register'
import { useRouter } from 'vue-router'
import useClipboard from 'vue-clipboard3'
import { ElMessage } from 'element-plus'

const { toClipboard } = useClipboard()
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
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<span class="text-xl">当前注册</span>
			</template>

			<el-table :data="showList" highlight-current-row>
				<el-table-column label="No." type="index" :index="indexMethod" width="100" />
				<el-table-column label="名称" prop="name" />
				<el-table-column label="ID" prop="id">
					<template #default="scope">
						<div class="cursor-pointer" @click="copyID(scope.row.id)">
							<span>{{ scope.row.id }}</span>
							<el-icon><CopyDocument /></el-icon>
						</div>
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

			<el-button class="w-full mt-20px" plain @click="router.push('/dashboard/api/register-form')" size="large">
				<el-icon><Plus /></el-icon>
				&nbsp新增API
			</el-button>
		</el-card>
	</div>
</template>

<style lang="less" scoped>
.operate {
	.el-button {
		padding: 0px;
	}
}
</style>
