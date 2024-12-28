import React, {useState, useCallback} from 'react';
import {Link, Outlet, useNavigate} from 'react-router-dom';
import axios from 'axios';
import MemberLogin from './components/MemberLogin';

// ... Home 컴포넌트 코드 ...

function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(!!sessionStorage.getItem('accessToken'));

    // 로그인 상태를 업데이트하는 함수
    const updateLoginStatus = useCallback((status) => {
        setIsLoggedIn(status);
    }, []);

    const handleLogout = async () => {
        try {
            const accessToken = sessionStorage.getItem('accessToken');
            // 서버에 로그아웃 요청
            await axios.post('/api/members/logout', null, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });

            // 세션 스토리지에서 accessToken 제거
            sessionStorage.removeItem('accessToken');

            // 쿠키에서 refreshToken 제거
            document.cookie = 'refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

            // 상태 업데이트
            setIsLoggedIn(false);

            // 로그인 페이지로 리다이렉트
            navigate('/member/login');
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    return (
        <div>
            {/* 네비게이션 메뉴 */}
            <nav>
                <ul>
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    <li>
                        <Link to="/member/list">Member List</Link>
                    </li>
                    <li>
                        <Link to="/member/register">Register</Link>
                    </li>
                    <li>
                        <Link to="/member/login">Login</Link>
                    </li>
                    {isLoggedIn && (
                        <li>
                            <button onClick={handleLogout}>Logout</button>
                        </li>
                    )}
                </ul>
            </nav>
            {/* Outlet에 props로 isLoggedIn과 updateLoginStatus 전달 */}
            <Outlet context={{isLoggedIn, updateLoginStatus}}/>
        </div>
    );
}

export default App;