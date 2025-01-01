import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {useToast} from "@/recoil/toast/useToast";

// Axios 인스턴스 생성 및 설정
const api = axios.create({
    baseURL: '/',
});

// Axios 요청 인터셉터 추가: 모든 API 요청에 Authorization 헤더 추가
api.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('accessToken');
        console.log('accessToken=', accessToken);
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
            console.log('Axios Request Config: ', config);
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 로그인 폼 스키마 (yup 사용)
const loginFormSchema = yup.object().shape({
    email: yup.string().email('올바른 이메일 형식이 아닙니다.').required('이메일을 입력해 주세요.'),
    password: yup.string().required('비밀번호를 입력해 주세요.'),
});

const MemberLogin = () => {
    const navigate = useNavigate();
    const toast = useToast();

    // react-hook-form 설정
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
        reset,
    } = useForm({
        resolver: yupResolver(loginFormSchema),
        mode: 'onSubmit',
    });

    // 로그인 뮤테이션 (react-query)
    const loginMutation = useMutation({
        mutationFn: (body) => api.post('/api/members/login', body), // 타입 지정 제거
        onSuccess: (data) => {
            const { accessToken, refreshToken } = data.data;
            const payload = jwtDecode(accessToken);

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('userInfo', JSON.stringify(payload));

            reset();
            toast.success('로그인 되었습니다.');
            navigate('/', { replace: true });
        },
        onError: (error) => {
            let errorMessage = '로그인에 실패했습니다.';
            if (axios.isAxiosError(error) && error.response) {
                const errorData = error.response.data;
                switch (errorData.code) {
                    case 'm001':
                        errorMessage = '이메일이 올바르지 않습니다.';
                        break;
                    case 'a005':
                        errorMessage = '비밀번호가 올바르지 않습니다.';
                        break;
                    default:
                        errorMessage = errorData.message || errorMessage;
                }
            }
            toast.error(errorMessage);
        },
    });

    // 폼 제출 처리
    const onSubmit = handleSubmit((data) => {
        loginMutation.mutate({
            email: data.email,
            password: data.password,
        });
    });

    return (
        <div className="bg-light">
            <div className="container" style={{ marginTop: '30px' }}>
                <div className="row">
                    <div className="col-4"></div>
                    <div className="col-4">
                        <div className="card">
                            <div className="card-body">
                                <div className="card-title">
                                    <h3>
                                        <strong>로그인</strong>
                                    </h3>
                                </div>
                                <hr />
                                <form onSubmit={onSubmit}>
                                    <table className="table table-borderless">
                                        <tbody>
                                        <tr>
                                            <td style={{ width: '45%' }}>
                                                <label className="col-form-label">이메일</label>
                                            </td>
                                            <td style={{ width: '55%' }}>
                                                <input
                                                    type="text"
                                                    placeholder="이메일"
                                                    className={`form-control ${
                                                        errors.email ? 'is-invalid' : ''
                                                    }`}
                                                    {...register('email')}
                                                />
                                                {errors.email && (
                                                    <div className="invalid-feedback">
                                                        {errors.email.message}
                                                    </div>
                                                )}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label className="col-form-label">비밀번호</label>
                                            </td>
                                            <td>
                                                <input
                                                    type="password"
                                                    placeholder="비밀번호"
                                                    className={`form-control ${
                                                        errors.password ? 'is-invalid' : ''
                                                    }`}
                                                    {...register('password')}
                                                />
                                                {errors.password && (
                                                    <div className="invalid-feedback">
                                                        {errors.password.message}
                                                    </div>
                                                )}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colSpan="2" className="center-buttons">
                                                <button
                                                    className="btn btn-primary"
                                                    type="submit"
                                                    style={{ marginRight: '5px' }}
                                                    disabled={isSubmitting}
                                                >
                                                    확인
                                                </button>
                                                <button
                                                    className="btn btn-secondary"
                                                    type="button"
                                                    onClick={() => reset()}
                                                    disabled={isSubmitting}
                                                >
                                                    취소
                                                </button>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>

                                <p className="mt-3">
                                    <span className="me-3">계정이 없으신가요? </span>
                                    <a href="/member/register">회원 가입</a>
                                </p>

                                {/* 소셜 로그인 버튼 */}
                                <div className="mt-3 mb-3">
                                    <div className="social-login-header">
                                        <span>소셜 계정으로 가입</span>
                                    </div>
                                    <div className="social-login-buttons">
                                        <a
                                            href="/oauth2/authorization/google"
                                            className="social-login-button"
                                        >
                                            <img
                                                src="/images/google-logo.png"
                                                alt="Google"
                                                className="social-login-icon"
                                            />
                                        </a>
                                        <a
                                            href="/oauth2/authorization/naver"
                                            className="social-login-button"
                                        >
                                            <img
                                                src="/images/naver-logo.jpg"
                                                alt="Naver"
                                                className="social-login-icon"
                                            />
                                        </a>
                                        <a
                                            href="/oauth2/authorization/kakao"
                                            className="social-login-button"
                                        >
                                            <img
                                                src="/images/kakao-logo.png"
                                                alt="Kakao"
                                                className="social-login-icon"
                                            />
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-4"></div>
                </div>
            </div>
        </div>
    );
};

export default MemberLogin;