export const validators = {
	required: (message?: string) => {
		return {
			required: true,
			message: (message || '') + '必填',
			trigger: 'blur',
		}
	},
	notEmpty: (message?: string) => {
		return {
			pattern: /\s*\S+?/,
			message: (message || '') + '不能为空字符串',
			trigger: 'blur',
		}
	},
	lengthRange: (min?: number, max?: number, message?: string) => {
		if (min && max) {
			return {
				min: min,
				max: max,
				message: (message || '') + `长度要在${min}~${max}之间`,
				trigger: 'blur',
			}
		} else if (!min && max) {
			return {
				max: max,
				message: (message || '') + `长度不能超过${max}`,
				trigger: 'blur',
			}
		} else if (min && !max) {
			return {
				min: min,
				message: (message || '') + `长度不能少于${min}`,
				trigger: 'blur',
			}
		} else {
			return {}
		}
	},
	url: () => {
		return {
			pattern: /^[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b(?:[-a-zA-Z0-9()@:%_\+.~#?&//=]*)$/,
			message: 'URL格式错误',
			trigger: 'blur',
		}
	},
	json: (rule: any, value: string, callback: any) => {
		if (value.trim() === '') {
			callback()
		} else {
			try {
				JSON.parse(value)
				callback()
			} catch (e) {
				callback('JSON格式错误')
			}
		}
	},
	email: () => {
		return {
			pattern: /^[0-9a-zA-Z_.-]+[@][0-9a-zA-Z_.-]+([.][a-zA-Z]+){1,2}$/,
			message: '邮箱格式错误',
			trigger: 'blur'
		}
	}
}
