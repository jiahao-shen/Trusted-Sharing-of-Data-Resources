import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/api',
      name: 'API',
      children: [
        {
          path: 'registration',
          name: '注册',
          component: () => import('../views/api/Registration.vue')
        },
        {
          path: 'request',
          name: '申请',
          component: () => import('../views/api/Request.vue')
        }
      ],
    }
  ]
})

export default router
