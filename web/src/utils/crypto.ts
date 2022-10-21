import CryptoJS from 'crypto-js'

export const encrypt = (password: string, key: string, method = 'AES') => {
	switch (method) {
		case 'AES':
			return CryptoJS.AES.encrypt(password, key).toString()
	}
}

export const decrypt = (password: string, key: string, method = 'AES') => {
	switch (method) {
		case 'AES':
			return CryptoJS.AES.decrypt(password, key).toString(CryptoJS.enc.Utf8)
	}
}