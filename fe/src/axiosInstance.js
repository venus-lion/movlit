// axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: process.env.VITE_BASE_URL,
    withCredentials: true,
});

// Request Interceptor
axiosInstance.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('accessToken'); // 세션 스토리지 -> 로컬 스토리지
        if (accessToken) {
            console.log('===== [accessToken이 존재하는 경우] 헤더에 accessToken을 추가합니다.')
            config.headers['Authorization'] = `Bearer ${accessToken}`;
            console.log('===== [accessToken이 존재하는 경우] 헤더 config', config)
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response Interceptor 추가
axiosInstance.interceptors.response.use(
    (response) => {
        // 응답 데이터에 accessToken이 있으면 localStorage에 저장
        if (response.data && response.data.accessToken) {
            console.log('accessToken = ', response.data.accessToken)
            localStorage.setItem('accessToken', response.data.accessToken);
        }
        return response;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;