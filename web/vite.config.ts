import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import WindiCSS from 'vite-plugin-windicss'
import PurgeIcons from 'vite-plugin-purge-icons'
import { viteMockServe } from 'vite-plugin-mock'

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [
		vue(),
		WindiCSS(),
		PurgeIcons(),
		viteMockServe({
			ignore: /^\_/,
			mockPath: 'mock',
			localEnabled: true,
			prodEnabled: false,
			injectCode: `
				import { setupProdMockServer } from '../mock/_createProductionServer'
				setupProdMockServer()
				`,
		}),
	],
	resolve: {
		alias: {
			'@': fileURLToPath(new URL('./src', import.meta.url)),
		},
	},
	server: {
		proxy: {
			'/api': {
				target: 'http://localhost:8080',
				changeOrigin: true,
				rewrite: path => path.replace(/^\/api/, '')
			}
		},
	},
	css: {
		preprocessorOptions: {
			less: {
				javascriptEnable: true,
			},
		},
	},
})
