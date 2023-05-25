import { v4 } from 'uuid'
import moment from 'moment'
import type { MockMethod } from 'vite-plugin-mock'

export default [
	{
		url: '/user/login',
		method: 'post',
		statusCode: 200,
		response: () => {
			return {
				username: '沈嘉浩',
				imageURL: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
			}
		},
	},
	{
		url: '/user/list',
		method: 'get',
		statusCode: 200,
		response: () => {
			return [
				{
					username: 'admin',
					type: 'NORMAL',
					permission: ['权限一', '权限二'],
					createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
				}
			]
		}
	}
] as MockMethod[]
