import { http } from '@/service'

export const service = {
	getAllAPIList: () => {
		return http.get('/fabric/api/allList')
	},
	// TODO: 参数
	getMyAPIList: () => {
		return http.get('/fabric/api/myList')
	}
}