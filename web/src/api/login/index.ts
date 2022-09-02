import { http } from '@/api'

export const service = {
	login: (username: string, password: string) => {
		return http.post('/user/login', {
			username: username,
			password: password,
		})
	},

	logout: (username: string) => {
		return http.post('/user/logout', {})
	}
}
