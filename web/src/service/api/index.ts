import { http } from '@/service'
import { BodyType } from '@/utils/enums'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()

export const apiService = {
	// API注册申请
	apiRegisterApply: (form: any) => {
		return http.post('/api/register/apply', {
			name: form.name,
			url: form.protocol + form.url,
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
	// 查询API注册申请列表
	apiRegisterApplyList: () => {
		return http.get('/api/register/apply/list')
	},
	// API注册申请回复
	apiRegisterReply: (serialNumber: string, reply: string | null, reason?: string) => {
		return http.post('/api/register/reply', {
			serialNumber: serialNumber,
			reply: reply,
			reason: reason,
		})
	},
	// 我的全部API列表
	myAPIList: () => {
		return http.get('/api/list/my')
	},
	// 所有的API列表
	allAPIList: () => {
		return http.get('/api/list/all')
	},
	// API调用请求
	apiInvokeApply: (form: any, apiID?: string) => {
		return http.post('/api/invoke/apply', {
			id: apiID,
			applicant: appStore.getUser.id,
			invokeMethod: form.invokeMethod,
			comment: form.comment,
			startTime: form.validTime[0],
			endTime: form.validTime[1],
			header: (() => {
				if (form.headerType === BodyType.FORM) {
					return form.headerList.length ? JSON.stringify(form.headerList) : ''
				} else if (form.headerType === BodyType.JSON) {
					return form.headerText
				}
			})(),
			request: (() => {
				if (form.requestType === BodyType.FORM) {
					return form.requestList.length ? JSON.stringify(form.requestList) : ''
				} else if (form.requestType === BodyType.JSON) {
					return form.requestText
				}
			})(),
		})
	},
	// API调用请求列表(发起方)
	apiInvokeApplyList: () => {
		return http.get('/api/invoke/apply/list')
	},
	// API调用请求列表(所有者)
	apiInvokeApprovalList: () => {
		return http.get('/api/invoke/approval/list')
	},
	// API调用请求回复
	apiInvokeReply: (serialNumber: string, reply: string | null, reason?: string) => {
		return http.post('/api/invoke/reply', {
			serialNumber: serialNumber,
			reply: reply,
			reason: reason
		})
	}
}
