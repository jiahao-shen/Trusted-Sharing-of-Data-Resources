import { http } from '@/service'
import { CodeToText } from 'element-china-area-data'

export const service = {
	registerOrganization: (form: any) => {
		return http.post('/register/organization', {
			name: form.name,
			logo: '', // TODO:
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
			file: '', // TODO:
		})
	},

	getOrganizationList: () => {
		return http.get('/register/organization/list')
	}
}
