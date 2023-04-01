import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
    server: {
        open: true,
        proxy: {
            '/v1/api': {
                target: 'http://127.0.0.1:8080',
                changeOrigin: true,
                ws: true
            }
        }
    },
    plugins: [vue()],
})
