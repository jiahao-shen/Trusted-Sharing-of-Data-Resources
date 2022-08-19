import { createRouter, createWebHistory } from "vue-router"
import HomeView from "../views/HomeView.vue"

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			// TODO:
			path: "/",
			name: "home",
			redirect: "/login",
		},
		{
			path: "/about",
			name: "about",
			component: () => import("../views/AboutView.vue"),
		},
		{
			path: "/api",
			name: "API",
			children: [
				{
					path: "registration",
					name: "注册",
					component: () => import("../views/api/Registration.vue"),
				},
				{
					path: "request",
					name: "申请",
					component: () => import("../views/api/Request.vue"),
				},
			],
		},
		{
			path: "/login",
			name: "login",
			component: () => import("../views/Login/Login.vue"),
		},
		{
			path: "/dashboard",
			name: "dashboard",
			component: () => import("../views/Dashboard/Dashboard.vue"),
		},
	],
})

export default router
