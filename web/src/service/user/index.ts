import { http } from '@/service'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()

export const userService = {
	userLogin: (form: any) => {
		return http.post('/user/login', {
			username: form.username,
			password: form.password, // TODO: 使用https传输
		})
	},
	userLogout: () => {
		return http.get('/user/logout')
	},
	userList: () => {
		return http.get('/user/list')
	},
	userExist: (username: string) => {
		return http.post('/user/exist', {
			username: username,
		})
	},
	userRegister: (form: any, organization: string, type: string) => {
		return http.post('/user/register', {
			username: form.username,
			password: form.password1,
			organization: organization,
			type: type,
		})
	},
}
