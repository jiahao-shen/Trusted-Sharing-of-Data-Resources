import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			// TODO:
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
							path: 'registration',
							name: 'API注册',
							component: () => import('../views/Dashboard/API/Registration.vue'),
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
	],
})

export default router
