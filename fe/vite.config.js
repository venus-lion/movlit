import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        port: 3000
        // proxy: {
        //     '/api': {
        //         target: 'https://movlit.store', // Spring Boot 백엔드 주소
        //         changeOrigin: true,
        //         // secure: false, // HTTPS가 아닌 경우 필요
        //         // rewrite: (path) => path.replace(/^\/api/, ''), // 필요에 따라 경로 재작성
        //     },
        //     '/oauth2': {
        //         target: 'https://movlit.store', // Spring Boot 백엔드 주소
        //         changeOrigin: true,
        //         // secure: false, // HTTPS가 아닌 경우 필요
        //         // rewrite: (path) => path.replace(/^\/oauth2/, ''), // 필요에 따라 경로 재작성
        //     },
        // },
    },
});