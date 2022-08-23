import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: '主页',
			redirect: '/login',
		},
		{
			path: '/about',
			name: '关于',
			component: () => import('../views/AboutView.vue'),
		},
		{
			path: '/login',
			name: '登录',
			component: () => import('../views/Login/Login.vue'),
		},
		{
			path: '/dashboard',
			name: '控制台',
			component: () => import('../views/Dashboard/Dashboard.vue'),
			children: [
				{
					path: 'api',
					name: 'API管理',
					children: [
						{
							path: 'register',
							name: 'API注册',
							component: () => import('../views/Dashboard/API/Register.vue'),
						},
						{
							path: 'request',
							name: 'API申请',
							component: () => import('../views/Dashboard/API/Request.vue'),
						},
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
					component: () => import('../views/Error/Error.vue'),
					props: {
						type: '403'
					},
				},
				{
					path: '404',
					component: () => import('../views/Error/Error.vue'),
					props: {
						type: '404'
					},
				},
				{
					path: '500',
					component: () => import('../views/Error/Error.vue'),
					props: {
						type: '500'
					},
				},
			]
		},
		{
			path: '/:path(.*)*',
			redirect: '/error/404',
		}
	],
})

export default router
