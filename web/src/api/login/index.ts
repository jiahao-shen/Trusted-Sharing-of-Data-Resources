import { http } from '@/api'

export const service = {
	login: (username: string, password: string) => {
		return http.post('/user/login', {
			username: username,
			password: password,
		})
	},
}
