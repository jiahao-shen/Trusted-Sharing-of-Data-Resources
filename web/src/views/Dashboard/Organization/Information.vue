<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/app'
import { OrgLogo } from '@/components/OrgLogo'
import { OrganizationType } from '@/utils/enums'
import { organizationService } from '@/service/organization'

const route = useRoute()
const appStore = useAppStore()
let organization = ref()

onMounted(() => {
	loadOrganizationInformation()
})

const loadOrganizationInformation = () => {
	organizationService
		.organizationInformation()
		.then((res: any) => {
			organization.value = res.data
			console.log(organization.value)
		})
		.catch((err: any) => {})
}
</script>
<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">机构详情</h2>
			</template>

			<div v-if="organization">
				<div class="w-full flex justify-center items-center">
					<OrgLogo :size="150" :src="organization.logo" />
				</div>
				<el-descriptions class="mt-20px" id="descriptions" :column="4">
					<el-descriptions-item label="机构名称">{{ organization.name }}</el-descriptions-item>
					<el-descriptions-item label="机构类型">
						<el-tag>{{ OrganizationType[organization.type] }}</el-tag>
					</el-descriptions-item>
					<el-descriptions-item label="机构电话">
						{{ organization.telephone }}
					</el-descriptions-item>
					<el-descriptions-item label="机构邮箱">
						<el-link :href="'mailto:' + organization.email">{{ organization.email }}</el-link>
					</el-descriptions-item>
					<el-descriptions-item label="所在城市">{{ organization.city }}</el-descriptions-item>
					<el-descriptions-item label="详细地址">{{ organization.address }}</el-descriptions-item>
					<el-descriptions-item label="上级机构">{{ organization.superior }}</el-descriptions-item>
					<el-descriptions-item label="创建时间">
						{{ organization.createdTime }}
					</el-descriptions-item>
					<el-descriptions-item label="机构介绍">{{ organization.introduction }}</el-descriptions-item>
				</el-descriptions>

				<div class="w-full flex justify-center items-center">
					<el-button icon="Edit" type="primary">编辑</el-button>
				</div>
			</div>
		</el-card>
	</div>
</template>
<style lang="less" scoped>
// #descriptions {
//   :deep(.el-descriptions__label) {
//     font-size: 1.2rem;
//   }
//   :deep(.el-descriptions__content) {
//     font-size: 1rem;
//   }
// }
</style>
