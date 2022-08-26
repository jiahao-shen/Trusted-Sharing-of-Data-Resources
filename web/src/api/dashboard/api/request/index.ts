import { http } from '@/api'

export const service = {
	getAPIList: () => {
		return http.get('/api/apiList')
	}
}