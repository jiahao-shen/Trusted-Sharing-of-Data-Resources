import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'

const http: AxiosInstance = axios.create({
	baseURL: '/api',
	timeout: 10000,
})

// http.interceptors.request.use((config: AxiosRequestConfig) => {
// 	// TODO:
// 	return config
// })

// http.interceptors.response.use((response: AxiosResponse<any>) => {
// 	// TODO:
// 	return response
// })

export { http }
