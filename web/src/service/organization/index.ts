import axios from 'axios'
import { http } from '@/service'
import { useAppStore } from '@/store/app'
import { CodeToText } from 'element-china-area-data'

const appStore = useAppStore()

export const organizationService = {
	organizationRegisterRequest: (form: any) => {
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

		return http.post('/organization/register/request', formData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		})
	},
	organizationSelectList: () => {
		return http.get('/organization/selectList')
	},
	organizationExist: (name: string) => {
		return http.post('/organization/exist', {
			name: name,
		})
	},
	organizationRegsiterRequestList: () => {
		return http.get('/organization/register/request/list')
	},
	organizationRegisterRequsetReply: (serialNumber: string, reply: string | null, reason?: string) => {
		return http.post('/organization/register/request/reply', {
			serialNumber: serialNumber,
			reply: reply,
			reason: reason,
		})
	},
	organizationInformation: () => {
		return http.post('/organization/information', {
			id: appStore.getUser.organization,
		})
	},
	searchRegisterProgress: (serialNumbers: string[]) => {
		return http.post('/organization/register/request/progress', {
			serialNumbers: serialNumbers,
		})
	},
}
