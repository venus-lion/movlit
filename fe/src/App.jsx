import React, { useCallback, useState, createContext, useEffect } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import axiosInstance from './axiosInstance';
import './App.css';

export const AppContext = createContext();

function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(
        !!sessionStorage.getItem('accessToken')
    );
    const [profileImage, setProfileImage] = useState(null); // 프로필 이미지 URL 상태
    console.log('profileImage = {}', profileImage);

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
            setProfileImage(null); // 로그아웃 시 프로필 이미지 상태 초기화
            navigate('/member/login');
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    useEffect(() => {
        const fetchProfileImage = async () => {
            try {
                const response = await axiosInstance.get('/images/profile');
                setProfileImage(response.data); // 응답으로 받은 이미지 URL을 상태에 저장
            } catch (error) {
                console.error('Error fetching profile image:', error);
                // 에러 처리 (예: 기본 이미지 설정)
                setProfileImage(null);
            }
        };

        if (isLoggedIn) {
            fetchProfileImage();
        }
    }, [isLoggedIn]);

    return (
        <AppContext.Provider value={{ updateLoginStatus }}>
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
                                {profileImage ? (
                                    <img src={profileImage.url} alt="프로필" className="nav-mypage-img" />
                                ) : (
                                    <div className="nav-mypage-img placeholder"></div>
                                )}
                            </NavLink>
                            <button onClick={handleLogout} className="logout-button">
                                로그아웃
                            </button>
                        </div>
                    )}
                </div>
            </nav>
            <Outlet context={{ updateLoginStatus }} />
        </AppContext.Provider>
    );
}

export default App;