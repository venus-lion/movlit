import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // Spring Boot 백엔드 주소
                changeOrigin: true,
                secure: false, // HTTPS가 아닌 경우 필요
                // rewrite: (path) => path.replace(/^\/api/, ''), // 필요에 따라 경로 재작성
            },
        },
    },
});