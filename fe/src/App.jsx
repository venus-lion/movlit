import React, { useCallback, useState, createContext } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import axiosInstance from './axiosInstance';
import './App.css';
import { FaUserCircle } from 'react-icons/fa';

export const AppContext = createContext();

function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(
        !!sessionStorage.getItem('accessToken')
    );

    const updateLoginStatus = useCallback((status) => {
        setIsLoggedIn(status);
    }, []);

    const handleLogout = async () => {
        try {
            await axiosInstance.post('/members/logout');
            sessionStorage.removeItem('accessToken');
            document.cookie =
                'refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
            updateLoginStatus(false);
            navigate('/member/login');
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    return (
        <AppContext.Provider value={{ updateLoginStatus, isLoggedIn }}>
            <nav className="navbar">
                <div className="nav-left">
                    <NavLink to="/" className={({ isActive }) => (isActive ? 'active' : '')}>
                        WATCHA PEDIA
                    </NavLink>
                    <NavLink to="/" className={({ isActive }) => (isActive ? 'active' : '')}>
                        영화
                    </NavLink>
                    <NavLink
                        to="/book"
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        책
                    </NavLink>
                </div>
                <div className="nav-right">
                    <input type="text" placeholder="검색어를 입력하세요" className="search-box" />
                    {!isLoggedIn && (
                        <>
                            <NavLink
                                to="/member/login"
                                className={({ isActive }) => (isActive ? 'active' : '')}
                            >
                                로그인
                            </NavLink>
                            <NavLink
                                to="/member/register"
                                className={({ isActive }) => (isActive ? 'active' : '')}
                            >
                                회원가입
                            </NavLink>
                        </>
                    )}
                    {isLoggedIn && (
                        <div className="nav-right-logged-in">
                            <NavLink
                                to="/mypage"
                                className={({ isActive }) => (isActive ? 'active nav-mypage' : 'nav-mypage')}
                            >
                                <FaUserCircle className="nav-mypage-icon" />
                            </NavLink>
                            <button onClick={handleLogout} className="logout-button">
                                로그아웃
                            </button>
                        </div>
                    )}
                </div>
            </nav>
            <Outlet context={{ updateLoginStatus, isLoggedIn }} />
        </AppContext.Provider>
    );
}

export default App;


