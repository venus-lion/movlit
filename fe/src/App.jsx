import React, { useCallback, useState, createContext, useEffect } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import axiosInstance from './axiosInstance';
import './App.css';
import { ToastContainer } from 'react-toastify';
import { FaUserCircle } from 'react-icons/fa';

export const AppContext = createContext();

function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(
        !!sessionStorage.getItem('accessToken')
    );
    const [profileImage, setProfileImage] = useState(null);
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
            setProfileImage(null);
            navigate('/member/login');
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    // Enter 키 이벤트 처리
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const [inputStr, setInputStr] = useState('');

    // 입력 값 변경 시 실행
    const handleInputChange = (event) => {
        setInputStr(event.target.value);
    };

    const handleSearch = async () => {
        if (inputStr.trim() === '') {
            alert('검색어를 입력해주세요!');
            return;
        }

        navigate(`/search/${encodeURIComponent(inputStr)}`);
    };

    useEffect(() => {
        const fetchProfileImage = async () => {
            try {
                const response = await axiosInstance.get('/images/profile');
                setProfileImage(response.data);
            } catch (error) {
                console.error('Error fetching profile image:', error);
                setProfileImage(null);
            }
        };

        if (isLoggedIn) {
            fetchProfileImage();
        }
    }, [isLoggedIn]);

    return (
        <AppContext.Provider value={{ updateLoginStatus, isLoggedIn }}>
            <nav className="navbar">
                <div className="nav-left">
                    <NavLink
                        to="/"
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        Movlit
                    </NavLink>
                    <NavLink
                        to="/"
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        영화
                    </NavLink>
                    <NavLink
                        to="/book"
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        책
                    </NavLink>
                    <NavLink
                        to="/chat" // 채팅 페이지로 이동하는 링크 추가
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        채팅
                    </NavLink>
                </div>
                <div className="nav-right">
                    <input
                        id={'searchInput'}
                        type="text"
                        placeholder="검색어를 입력하세요"
                        className="search-box"
                        onChange={(e) => setInputStr(e.target.value)}
                        onKeyDown={(e) => handleKeyDown(e)}
                    />
                    <button className="search-button" onClick={handleSearch}>
                        검색
                    </button>
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
                                className={({ isActive }) =>
                                    isActive ? 'active nav-mypage' : 'nav-mypage'
                                }
                            >
                                {profileImage ? (
                                    <img
                                        src={profileImage.url}
                                        alt="프로필"
                                        className="nav-mypage-img"
                                    />
                                ) : (
                                    <FaUserCircle className="nav-mypage-icon" />
                                )}
                            </NavLink>
                            <button onClick={handleLogout} className="logout-button">
                                로그아웃
                            </button>
                        </div>
                    )}
                </div>
            </nav>

            {/* Outlet에 context 전달 */}
            <Outlet context={{ updateLoginStatus, isLoggedIn }} />

            <ToastContainer />
        </AppContext.Provider>
    );
}

export default App;