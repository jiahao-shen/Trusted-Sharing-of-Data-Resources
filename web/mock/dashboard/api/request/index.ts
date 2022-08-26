import { v4 } from 'uuid'
import moment from 'moment'
import type { MockMethod } from 'vite-plugin-mock'

const randomChoice = (array: any[]) => {
	return array[Math.floor(Math.random() * array.length)]
}

const statusList = ['可用', '不可用']
const orgList = [
	{
		name: '机构一',
		class: '金融',
	},
	{
		name: '机构二',
		class: '教育',
	},
	{
		name: '机构三',
		class: '医疗',
	},
	{
		name: '机构四',
		class: '交通',
	},
]
const functionList = ['功能一', '功能二', '功能三', '功能四']

export default [
	{
		url: '/api/apiList',
		method: 'get',
		statusCode: 200,
		response: () => {
			let apiList = []
			for (let i = 1; i <= 200; ++i) {
				let org = randomChoice(orgList)
				apiList.push({
					orgName: org.name,
					orgClass: org.class,
					apiName: `API-${i}`,
					apiID: v4(),
					apiFunction: randomChoice(functionList),
					apiStatus: randomChoice(statusList),
				})
			}
			return apiList
		},
	},
] as MockMethod[]
