import axios from 'axios'
import { useAppStore } from '@/store/app'
import { HttpStatusCode } from '@/utils/enums'
import { useRoute, useRouter } from 'vue-router'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'

const router = useRouter()
const appStore = useAppStore()

const http: AxiosInstance = axios.create({
	baseURL: '/server',
	timeout: 10000,
})

http.interceptors.request.use((config: AxiosRequestConfig) => {
	// TODO:
	return config
})

http.interceptors.response.use(
	(response: AxiosResponse<any>) => {
		return response
	},
	(error: AxiosError) => {
		switch (error.response?.status) {
			case HttpStatusCode.Unauthorized:
				appStore.setUser(null)
				router.push('/login')
				break
		}
	}
)

export { http }
