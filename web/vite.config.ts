import { fileURLToPath, URL } from "node:url"

import { defineConfig } from "vite"
import vue from "@vitejs/plugin-vue"
import WindiCSS from "vite-plugin-windicss"
import PurgeIcons from "vite-plugin-purge-icons"

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [vue(), WindiCSS(), PurgeIcons()],
	resolve: {
		alias: {
			"@": fileURLToPath(new URL("./src", import.meta.url)),
		},
	},
})
