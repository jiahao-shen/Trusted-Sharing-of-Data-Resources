import { http } from '@/service'
import { Axios } from 'axios';

export const service = {
	registerAPI: (form: any) => {
		return http.post('/fabric/api/register', {
			name: form.name,
			url: form.protocol + '://' + form.url,
			method: form.method,
			introduction: form.introduction,
			category: form.category,
			version: form.version,
			permission: form.permission,
			header: (() => {
				if (form.headerType === 'form') {
					return form.headerList.length ? JSON.stringify(form.headerList) : ''
				} else if (form.headerType === 'json') {
					return form.headerText
				}
			})(),
			headerType: form.headerType,
			request: (() => {
				if (form.requestType === 'form') {
					return form.requestList.length ? JSON.stringify(form.requestList) : ''
				} else if (form.requestType === 'json') {
					return form.requestText
				}
			})(),
			requestType: form.requestType,
			response: (() => {
				if (form.responseType === 'form') {
					return form.responseList.length ? JSON.stringify(form.responseList) : ''
				} else if (form.responseType === 'json') {
					return form.responseText
				}
			})(),
			responseType: form.responseType,
		})
	},
	getRegisterList: () => {
		return http.get('/api/registerList')
	},
}
