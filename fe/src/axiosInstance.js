// axiosInstance.js (또는 설정 파일)
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: '/api', // 기본 URL 설정
});

// Request Interceptor
axiosInstance.interceptors.request.use(
    (config) => {
        const accessToken = sessionStorage.getItem('accessToken');
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);


export default axiosInstance;