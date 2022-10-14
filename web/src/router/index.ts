import { createRouter, createWebHistory } from 'vue-router'
import { useAppStore } from '@/store/app'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: '主页',
			redirect: '/dashboard',
		},
		{
			path: '/about',
			name: '关于',
			component: () => import('@/views/AboutView.vue'),
		},
		{
			path: '/login',
			name: '登录',
			component: () => import('@/views/Login/Login.vue'),
		},
		{
			path: '/register',
			name: '注册',
			component: () => import('@/views/Register/Register.vue'),
		},
		{
			path: '/dashboard',
			name: '控制台',
			component: () => import('@/views/Dashboard/Dashboard.vue'),
			children: [
				{
					path: 'api',
					name: 'API管理',
					component: () => import('@/views/Dashboard/API/API.vue'),
					children: [
						{
							path: 'register',
							name: 'API注册',
							component: () => import('@/views/Dashboard/API/Register.vue'),
							children: [
								{
									path: 'form',
									name: '注册表单',
									component: () => import('@/views/Dashboard/API/components/RegisterForm.vue'),
								},
							],
						},
						{
							path: 'request',
							name: 'API申请',
							component: () => import('@/views/Dashboard/API/Request.vue'),
							children: [
								{
									path: 'form',
									name: '申请表单',
									component: () => import('@/views/Dashboard/API/components/RequestForm.vue'),
								},
							],
						},
					],
				},
				{
					path: 'organization',
					name: '机构相关',
					component: () => import('@/views/Dashboard/Organization/Organization.vue'),
					children: [
						{
							path: 'information',
							name: '机构详情',
							component: () => import('@/views/Dashboard/Organization/Information.vue'),
						},
						{
							path: 'user',
							name: '用户管理',
							component: () => import('@/views/Dashboard/Organization/User.vue')
						},
						{
							path: 'approval',
							name: '审批流程',
							component: () => import('@/views/Dashboard/Organization/Approval.vue')
						}
					],
				},
			],
		},
		{
			path: '/error',
			redirect: '/error/404',
			children: [
				{
					path: '403',
					component: () => import('@/views/Error/Error.vue'),
					props: {
						type: '403',
					},
				},
				{
					path: '404',
					component: () => import('@/views/Error/Error.vue'),
					props: {
						type: '404',
					},
				},
				{
					path: '500',
					component: () => import('@/views/Error/Error.vue'),
					props: {
						type: '500',
					},
				},
			],
		},
		{
			path: '/:path(.*)*',
			redirect: '/error/404',
		},
	],
})

router.beforeEach((to, from, next) => {
	const appStore = useAppStore()

	if (appStore.getUser === null && to.path !== '/login' && to.path !== '/register') {
		next('/login')
	} else {
		next()
	}
})

export default router
