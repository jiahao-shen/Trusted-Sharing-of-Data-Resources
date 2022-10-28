import axios from 'axios'
import { http } from '@/service'
import { useAppStore } from '@/store/app'
import { CodeToText } from 'element-china-area-data'

const appStore = useAppStore()

export const organizationService = {
	// 发起机构注册申请
	organizationRegisterApply: (form: any) => {
		let info = {
			name: form.name,
			logo: '',
			type: form.type,
			telephone: form.telephone,
			email: form.email,
			city: (() => {
				let tmp: any[] = []
				form.city.forEach((item: any) => {
					tmp.push(CodeToText[item])
				})
				return tmp.join(',')
			})(),
			address: form.address,
			introduction: form.introduction,
			superior: form.superior,
			provideNode: form.provideNode,
			numNodes: form.provideNode ? form.numNodes : 0,
			file: '',
		}
		const formData = new FormData()
		formData.append('logo', form.logo[0].raw, form.logo[0].name)
		formData.append('info', JSON.stringify(info))
		formData.append('file', form.file[0].raw, form.file[0].name)

		return http.post('/organization/register/apply', formData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		})
	},
	// 查询机构申请列表
	organizationRegisterApplyList: (serialNumbers: string[]) => {
		return http.post('/organization/register/apply/list', {
			serialNumbers: serialNumbers,
		})
	},
	// 获取机构审批列表
	organizationRegsiterApprovalList: () => {
		return http.get('/organization/register/approval/list')
	},
	// 机构注册申请回复
	organizationRegisterReply: (serialNumber: string, reply: string | null, reason?: string) => {
		return http.post('/organization/register/reply', {
			serialNumber: serialNumber,
			reply: reply,
			reason: reason,
		})
	},
	// 获取列表用于下拉框
	organizationSelectList: () => {
		return http.get('/organization/selectList')
	},
	// 查询机构是否存在
	organizationExist: (name: string) => {
		return http.post('/organization/exist', {
			name: name,
		})
	},
	// 获取机构信息
	organizationInformation: () => {
		console.log(appStore.getUser.organization)
		return http.post('/organization/information', {
			id: appStore.getUser.organization,
		})
	},
	// 获取下级机构列表
	organizationSubordinateList: () => {
		return http.get('/organization/subordinate/list')
	}
}
