<script setup lang="ts">
import moment from 'moment'
import { useRoute } from 'vue-router'

const route = useRoute()
const organization = {
	logo: 'https://www.buaa.edu.cn/__local/B/CD/A6/968D8F6600C0B8195CD59008BF5_9249A080_12AB7.jpg?e=.jpg',
	name: '北京航空航天大学',
	type: '教育',
	email: '1843781563@qq.com',
	telephone: '010-82317114',
	city: '北京市-海淀区',
	address: '学院路37号',
	introduction:
		'北京航空航天大学是中华人民共和国顶尖高校之一，是“双一流A类”和原“985工程”、原“211工程”重点建设大学，同時亦入选珠峰计划、2011计划、111计划、卓越工程师教育培养计划，为国际宇航联合会、欧洲顶尖工业管理者高校联盟、中欧精英大学联盟、中国西班牙大学联盟、中俄工科大学联盟、中俄综合性大学联盟成员。2018年10月，北航拥有42个学院、研究院，覆盖工、理、管、医、法、文等10个学科门类，是以工科为主的综合性研究型高等学府；北航拥有1个国家实验室（筹）、9个国家重点实验室和一系列省部级实验室，承担了“月宫一号”等重大科研项目；北航还参与、组织了万维网联盟等23个协会组织。',
	superior: '教育部',
	createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
}
</script>
<template>
	<div class="w-full p-20px">
		<el-card class="w-full">
			<template #header>
				<h2 class="text-2xl">机构详情</h2>
			</template>

			<div class="w-full flex justify-center items-center">
				<el-avatar :size="150" :src="organization.logo" />
			</div>

			<el-descriptions class="mt-20px" id="descriptions">
				<el-descriptions-item label="机构名称">{{ organization.name }}</el-descriptions-item>
        <el-descriptions-item label="机构类型">
          <el-tag>{{ organization.type }}</el-tag>
        </el-descriptions-item>
				<el-descriptions-item label="机构邮箱">
          <el-link :href="'mailto:' + organization.email">{{ organization.email }}</el-link>
        </el-descriptions-item> 
        <el-descriptions-item label="所在城市">{{ organization.city }}</el-descriptions-item>
        <el-descriptions-item label="详细地址">{{ organization.address }}</el-descriptions-item>
        <el-descriptions-item label="上级机构">{{ organization.superior }}</el-descriptions-item>
        <el-descriptions-item label="机构介绍">{{ organization.introduction }}</el-descriptions-item>
			</el-descriptions>

      <div class="w-full flex justify-center items-center">
        <el-button icon="Edit" type="primary">编辑</el-button>
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
