import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ command, mode }) => {
    const env = loadEnv(mode, process.cwd(), '');

    const isProduction = mode === 'production'; // mode 사용
    const baseUrl = env.VITE_BASE_URL || 'http://localhost:8080'; // VITE_BASE_URL 사용

    const config = {
        plugins: [react()],
        server: {
            port: 3000,
        },
    };

    if (!isProduction) {
        config.server.proxy = {
            '/api': {
                target: baseUrl,
                changeOrigin: true,
                secure: false,
            },
            '/oauth2': { // 배포 환경에서 이걸 추가해야 하나?
                target: baseUrl,
                changeOrigin: true,
                secure: false,
            },
        };
    }

    return config;
});