import { http } from '@/service'

export const service = {
	getAPIList: () => {
		return http.get('/api/apiList')
	}
}