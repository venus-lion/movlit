import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
    const env = loadEnv(mode, process.cwd(), ''); // 모든 환경 변수를 로드합니다.

    const isDev = env.VITE_IS_DEV === 'true'; // VITE_IS_DEV 값이 'true'인지 확인

    const proxyConfig = {};

    // 개발 모드인 경우에만 '/api' 프록시를 추가
    if (isDev) {
        proxyConfig['/api'] = {
            target: `${env.VITE_BASE_URL_FOR_CONF}`,
            changeOrigin: true,
            secure: false,
        };
        proxyConfig['/oauth2'] = {
            target: `${env.VITE_BASE_URL_FOR_CONF}`,
            changeOrigin: true,
            secure: false,
        };
        proxyConfig['/app'] = {
            target: `${env.VITE_BASE_URL_FOR_CONF}`,
            changeOrigin: true,
            secure: false,
        };
        // WebSocket 프록시 설정 추가
        proxyConfig['/ws-stomp'] = {
            target: `${env.VITE_BASE_URL_FOR_CONF}`, // WebSocket endpoint
            ws: true, // IMPORTANT: 웹소켓 프록시 활성화
            changeOrigin: true,
            secure: false,
        };
    }

    const config = {
        plugins: [react()],
        define: {
            'process.env': env,
            // 'global'을 정의하여 sockjs-client 오류 해결
            global: 'globalThis',
        },
        server: {
            port: 3000,
            proxy: proxyConfig, // 동적으로 구성된 proxyConfig 사용
        },
    };

    return config;
});