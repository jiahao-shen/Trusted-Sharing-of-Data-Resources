import { http } from '@/service'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()

export const userService = {
	userLogin: (form: any) => {
		return http.post('/user/login', {
			id: form.id,
			password: form.password, // TODO: 使用https传输
		})
	},
	userLogout: () => {
		return http.get('/user/logout')
	},
	userList: () => {
		return http.get('/user/list')
	},
	userExist: (id: string) => {
		return http.post('/user/exist', {
			id: id,
		})
	},
	userRegister: (form: any) => {
		return http.post('/user/register', {
			id: form.id,
			password: form.password1,
			organization: appStore.getUser.organization,
		})
	},
}
