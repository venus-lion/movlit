import { defineConfig, PluginOption } from 'vite';
import react from '@vitejs/plugin-react-swc'; // plugin-react 대신 plugin-react-swc 사용
import { visualizer } from 'rollup-plugin-visualizer';
import svgr from 'vite-plugin-svgr';
import tsconfigPaths from 'vite-tsconfig-paths';
import path from 'path';

export default defineConfig({
    plugins: [
        react(),
        svgr(),
        tsconfigPaths(),
        visualizer({
            filename: './dist/report.html',
            open: true,
            brotliSize: true,
        }) as PluginOption,
    ],
    optimizeDeps: {
        include: ['emoji-picker-react'],
    },
    resolve: {
        alias: {
            '@': path.resolve(__dirname, 'src'),
            '@hooks': path.resolve(__dirname, 'src/hooks'),
            '@recoil': path.resolve(__dirname, 'src/recoil'),
            // ... 필요한 별칭 추가
        },
    },
    server: { // 프록시 설정 추가 (필요한 경우)
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