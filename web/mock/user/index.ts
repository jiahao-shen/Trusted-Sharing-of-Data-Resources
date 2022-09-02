import type { MockMethod } from 'vite-plugin-mock'

export default [
	{
		url: '/user/login',
		method: 'get',
		statusCode: 200,
		response: () => {
			return {
				username: '沈嘉浩',
				imageURL: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
			}
		},
	},
] as MockMethod[]
