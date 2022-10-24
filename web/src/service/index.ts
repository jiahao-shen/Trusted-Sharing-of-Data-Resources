import axios from 'axios'
import { useAppStore } from '@/store/app'
import { ElNotification } from 'element-plus'
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
	(res: AxiosResponse<any>) => {
		return res;
	},
	(err: AxiosError) => {
		switch (err.response?.status) {
			case HttpStatusCode.Unauthorized: // 401:未登录
				appStore.setUser(null)
				router.push('/login')
				break
			case HttpStatusCode.NotFound: // 404:不存在
				ElNotification({
					title: '网络请求不存在',
					type: 'error',
				})
				break
			case HttpStatusCode.InternalServerError:	// 500: 服务器内部错误
				return Promise.reject(err)
			default:
				ElNotification({
					title: '未知错误',
					message: String(err.response?.data),
					type: 'error',
				})
		}
	}
)

export { http }
