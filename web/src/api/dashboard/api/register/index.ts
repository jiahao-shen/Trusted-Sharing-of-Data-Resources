import { http } from '@/api'

export const service = {
	getRegisterList: () => {
		return http.get('/api/registerList')
	}
}