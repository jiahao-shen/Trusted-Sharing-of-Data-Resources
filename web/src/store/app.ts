import { defineStore } from 'pinia'

export const useAppStore = defineStore({
	id: 'app',
	state: () => ({
		user: null,
		collapse: false,
	}),
	persist: {
		enabled: true,
	},
	getters: {
		getCollapse(): boolean {
			return this.collapse
		},
    getUser(): any {
      return this.user
    }
	},
	actions: {
		setCollapse(collapse: boolean) {
			this.collapse = collapse
		},
		setUser(user: any) {
			this.user = user
		},
	},
})
