import { http } from '@/api'
import sha256 from 'crypto-js/sha256'

export const service = {
	login: (username: string, password: string) => {
		return http.post('/user/login', {
			username: username,
			password: sha256(password).toString(),
		})
	},

	logout: (username: string) => {
		return http.post('/user/logout', {})
	}
}
