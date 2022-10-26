import { http } from '@/service'
import { BodyType } from '@/utils/enums'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()

export const apiService = {
	apiRegisterApply: (form: any) => {
		return http.post('/api/register/apply', {
			name: form.name,
			url: form.protocol + '://' + form.url,
			method: form.method,
			introduction: form.introduction,
			category: form.category,
			version: form.version,
			permission: form.permission,
			headerType: form.headerType,
			header: (() => {
				if (form.headerType === BodyType.FORM) {
					return form.headerList.length ? JSON.stringify(form.headerList) : ''
				} else if (form.headerType === BodyType.JSON) {
					return form.headerText
				}
			})(),
			requestType: form.requestType,
			request: (() => {
				if (form.requestType === BodyType.FORM) {
					return form.requestList.length ? JSON.stringify(form.requestList) : ''
				} else if (form.requestType === BodyType.JSON) {
					return form.requestText
				}
			})(),
			responseType: form.responseType,
			response: (() => {
				if (form.responseType === BodyType.FORM) {
					return form.responseList.length ? JSON.stringify(form.responseList) : ''
				} else if (form.responseType === BodyType.JSON) {
					return form.responseText
				}
			})(),
			author: appStore.getUser.id,
		})
	},
	apiRegisterApplyList: () => {
		return http.get('/api/register/apply/list')
	},
	apiRegisterReply: (serialNumber: string, reply: string | null, reason?: string) => {
		return http.post('/api/register/reply', {
			serialNumber: serialNumber,
			reply: reply,
			reason: reason,
		})
	},
	apiListMy: () => {
		return http.get('/api/list/my')
	},
	apiListAll: () => {
		return http.get('/api/list/all')
	}
	// callAPI: (form: any) => {
	// 	return http.post('/fabric/api/call', {
	// 		apiID: form.apiID,
	// 		apiHeader: (() => {
	// 			if (form.headerType === 'form') {
	// 				return form.headerList.length ? JSON.stringify(form.headerList) : ''
	// 			} else if (form.headerType === 'json') {
	// 				return form.headerText
	// 			}
	// 		})(),
	// 		apiRequest: (() => {
	// 			if (form.requestType === 'form') {
	// 				return form.requestList.length ? JSON.stringify(form.requestList) : ''
	// 			} else if (form.requestType === 'json') {
	// 				return form.requestText
	// 			}
	// 		})(),
	// 	})
	// },
}
