import React, {useState} from 'react';
import {Link, Outlet, useNavigate} from 'react-router-dom';
import axios from 'axios';

// 기존 App.jsx의 내용 (Vite + React 로고 및 카운터)을
// 별도의 컴포넌트로 분리
function Home() {
    const [count, setCount] = useState(0);

    return (
        <>
            <div>
                <a href="https://vitejs.dev" target="_blank" rel="noreferrer">
                    <img src="/vite.svg" className="logo" alt="Vite logo"/>
                </a>
                <a href="https://react.dev" target="_blank" rel="noreferrer">
                    <img src="/react.svg" className="logo react" alt="React logo"/>
                </a>
            </div>
            <h1>Vite + React</h1>
            <div className="card">
                <button onClick={() => setCount((count) => count + 1)}>
                    count is {count}
                </button>
                <p>
                    Edit <code>src/App.jsx</code> and save to test HMR
                </p>
            </div>
            <p className="read-the-docs">
                Click on the Vite and React logos to learn more
            </p>
        </>
    );
}

function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(!!sessionStorage.getItem('accessToken'));

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
            <Outlet/>
        </div>
    );
}

export default App;