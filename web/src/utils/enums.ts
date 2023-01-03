export enum APIInvokeMethod {
	WEB = '网页',
	SDK = 'SDK',
}

export enum InternetProtocol {
	HTTP = 'http://',
	HTTPS = 'https://',
}

export enum HttpMethod {
	GET = 'GET',
	POST = 'POST',
	PUT = 'PUT',
	DELETE = 'DELETE',
	TRACE = 'TRACE',
	CONNECT = 'CONNECT',
	HEAD = 'HEAD',
	OPTIONS = 'OPTIONS',
}

export enum BodyType {
	FORM = 'FORM',
	JSON = 'JSON',
}

export enum UserType {
	ADMIN = '管理员',
	NORMAL = '普通用户',
}

export enum OrganizationType {
	MEDICAL = '医疗',
	EDUCATION = '教育',
	FINANCIAL = '金融',
	GOVERNMENT = '政府',
}

export enum RegisterStatus {
	PROCESSED = '待处理',
	ALLOW = '通过',
	REJECT = '驳回',
}
