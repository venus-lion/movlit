import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';

const MemberLogin = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        // 페이지 로드 시 저장된 Access Token 확인 (옵션)
        const accessToken = sessionStorage.getItem('accessToken');
        if (accessToken) {
            // Access Token이 유효한 경우, 자동으로 /member/list로 리다이렉트
            // (필요한 경우, 서버에 Access Token 유효성 검증 요청 추가)
            navigate('/member/list');
        }
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/api/members/login', {email, password});
            const {accessToken, refreshToken} = response.data;

            // 토큰 저장
            sessionStorage.setItem('accessToken', accessToken);
            document.cookie = `refreshToken=${refreshToken}; Secure; HttpOnly; Path=/; Max-Age=1209600`; // 2주

            // 로그인 성공 후 페이지 리디렉션
            navigate('/member/list');
        } catch (error) {
            setError('로그인 정보가 올바르지 않습니다.');
            console.error('Login error:', error);
        }
    };

    // API 요청에 Access Token을 포함하는 함수 (예시)
    const fetchMemberList = async () => {
        try {
            const accessToken = sessionStorage.getItem('accessToken');
            const response = await axios.get('/member/list', {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            // ... API 응답 처리 ...
        } catch (error) {
            if (error.response && error.response.status === 401) {
                // Access Token 만료 시 Refresh Token으로 재발급 요청
                await refreshAccessToken();
                // ... 새로운 Access Token으로 API 요청 다시 시도 ...
            } else {
                console.error('API 요청 에러:', error);
            }
        }
    };

    // Refresh Token으로 Access Token을 재발급 받는 함수
    const refreshAccessToken = async () => {
        try {
            const refreshToken = document.cookie
                .split('; ')
                .find((row) => row.startsWith('refreshToken='))
                ?.split('=')[1];

            const response = await axios.post('/refresh', {refreshToken}); // /refresh 엔드포인트
            const {accessToken: newAccessToken} = response.data;

            // 새로운 Access Token 저장
            sessionStorage.setItem('accessToken', newAccessToken);
        } catch (error) {
            console.error('Refresh Token 에러:', error);
            // Refresh Token 만료 시 로그인 페이지로 리다이렉트
            navigate('/member/login');
        }
    };

    return (
        <div className="bg-light">
            {/* ... 상단 네비게이션 ... */}
            <div className="container" style={{marginTop: '270px'}}>
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
                                <hr/>
                                <form onSubmit={handleSubmit}>
                                    <table className="table table-borderless">
                                        <tbody>
                                        <tr>
                                            <td style={{width: '45%'}}>
                                                <label className="col-form-label">이메일</label>
                                            </td>
                                            <td style={{width: '55%'}}>
                                                <input
                                                    type="text"
                                                    name="email"
                                                    className="form-control"
                                                    value={email}
                                                    onChange={(e) => setEmail(e.target.value)}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label className="col-form-label">비밀번호</label>
                                            </td>
                                            <td>
                                                <input
                                                    type="password"
                                                    name="password"
                                                    className="form-control"
                                                    value={password}
                                                    onChange={(e) => setPassword(e.target.value)}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colSpan="2">
                                                <button className="btn btn-primary" type="submit">
                                                    확인
                                                </button>
                                                <button className="btn btn-secondary" type="reset">
                                                    취소
                                                </button>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>

                                {error && <p style={{color: 'red'}}>{error}</p>}

                                <p className="mt-3">
                                    <span className="me-3">사용자 계정이 없으신가요?</span>
                                    <a href="/member/register">사용자 가입</a>
                                </p>

                                {/* ... 소셜 로그인 ... */}
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