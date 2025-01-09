import React, { useEffect } from 'react';
import { useLocation, useNavigate, useOutletContext } from 'react-router-dom';

const OAuthCallback = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { updateLoginStatus } = useOutletContext();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const accessToken = queryParams.get('accessToken');
        const refreshToken = queryParams.get('refreshToken');

        console.log('accessToken:', accessToken); // accessToken 출력
        console.log('refreshToken:', refreshToken); // refreshToken 출력

        if (accessToken && refreshToken) {
            sessionStorage.setItem('accessToken', accessToken);
            document.cookie = `refreshToken=${refreshToken}; Secure; HttpOnly; Path=/; Max-Age=1209600`;

            console.error('OAuth2 로그인 성공, accessToken=', accessToken);
            updateLoginStatus(true);
            navigate('/'); // 메인 페이지로 리다이렉트
        } else {
            console.error('OAuth2 로그인 실패: 토큰이 없습니다.');
            navigate('/member/login'); // 로그인 페이지로 리다이렉트
        }
    }, [location, navigate, updateLoginStatus]);

    return <div>OAuth2 로그인 처리 중...</div>;
};

export default OAuthCallback;