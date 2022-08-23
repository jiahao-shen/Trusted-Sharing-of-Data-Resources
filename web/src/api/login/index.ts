import { http } from '@/api'

export const service = {
	login: () => {
		return http.get('/user/login')
	}
}