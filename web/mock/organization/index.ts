import { v4 } from 'uuid'
import moment from 'moment'
import type { MockMethod } from 'vite-plugin-mock'

export default [
	{
		url: '/organization/register/apply/list',
		method: 'post',
		statusCode: 200,
		response: () => {
			return [
				{
					serialNumber: v4(),
					name: '北京航空航天大学',
					type: 'EDUCATION',
					status: 'ALLOW',
					applyTime: moment().format('YYYY-MM-DD HH:mm:ss'),
				},
			]
		},
	},
	{
		url: '/organization/information',
		method: 'post',
		statusCode: 200,
		response: () => {
			return {
				logo: 'https://www.buaa.edu.cn/__local/B/CD/A6/968D8F6600C0B8195CD59008BF5_9249A080_12AB7.jpg?e=.jpg',
				name: '北京航空航天大学',
				type: 'EDUCATION',
				telephone: '13915558435',
				email: '1843781563@qq.com',
				city: '北京',
				address: '北京市海淀区学院路37号',
				superiorName: '教育部',
				createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
				introduction:
					'北京航空航天大学（Beihang University）简称“北航”，位于首都北京市，是中华人民共和国工业和信息化部直属的全国重点大学，中央直管高校， 位列国家“双一流”、“985工程”、“211工程”重点建设高校，入选珠峰计划、2011计划、111计划、卓越工程师教育培养计划、国家建设高水平大学公派研究生项目、中国政府奖学金来华留学生接收院校、国家级新工科研究与实践项目、国家级大学生创新创业训练计划、国家大学生创新性实验计划、全国深化创新创业教育改革示范高校、强基计划试点高校，为国际宇航联合会、中欧精英大学联盟、中国-西班牙大学联盟、中俄工科大学联盟、中国高校行星科学联盟、中国人工智能教育联席会、全国高等军工院校课程思政联盟、W3C组织成员。',
			}
		},
	},
	{
		url: '/organization/register/approval/list',
		method: 'get',
		statusCode: 200,
		response: () => {
			return [
				{
					serialNumber: v4(),
					name: '北京航空航天大学计算机学院',
					type: 'EDUCATION',
					status: 'PROCESSED',
					applyTime: moment().format('YYYY-MM-DD HH:mm:ss'),
				},
			]
		},
	},
  {
		url: '/organization/subordinate/list',
		method: 'get',
		statusCode: 200,
		response: () => {
			return [
				{
					id: v4(),
					name: '北京航空航天大学计算机学院',
					type: 'EDUCATION',
					createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
				},
			]
		},
	},
] as MockMethod[]
