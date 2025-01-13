// axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: process.env.VITE_BASE_URL,
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

// Response Interceptor 추가
axiosInstance.interceptors.response.use(
    (response) => {
        // 응답 데이터에 accessToken이 있으면 sessionStorage에 저장
        if (response.data && response.data.accessToken) {
            console.log('accessToken = ', response.data.accessToken)
            sessionStorage.setItem('accessToken', response.data.accessToken);
        }
        return response;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;