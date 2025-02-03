import React, { useCallback, useState, createContext, useEffect } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import axiosInstance from './axiosInstance';
import './App.css';
import { ToastContainer } from 'react-toastify';
import { FaUserCircle } from 'react-icons/fa';
import { EventSourcePolyfill } from 'event-source-polyfill';


export const AppContext = createContext();


function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(
        !!localStorage.getItem('accessToken') // 세션 스토리지 -> 로컬 스토리지
    );
    const [profileImage, setProfileImage] = useState(null);
    console.log('profileImage = {}', profileImage);

    const updateLoginStatus = useCallback((status) => {
        setIsLoggedIn(status);
    }, []);
    // 알림 관련
    const [notifications, setNotifications] = useState([]);
    const [newNotification, setNewNotification] = useState(false);

    const handleLogout = async () => {
        try {
            await axiosInstance.post('/members/logout');
            localStorage.removeItem('accessToken'); // 세션 스토리지 -> 로컬 스토리지
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

    // 알림 클릭 시
    const handleBellClick = () => {
        setNewNotification(false); // 종모양 클릭 시 새로운 알림 플래그 초기화
        navigate('/notifications'); // 알림 페이지로 이동
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

    useEffect(() => {
        let eventSource = null;
        let reconnectTimer = null;

        const setupSSE = async () => {
            try {
                // 사용자의 ID를 가져오는 API 호출
                const profileRes = await axiosInstance.get('/members/id');
                const userId = profileRes.data.memberId;

                if (Notification.permission !== 'granted') {
                    await Notification.requestPermission();
                }

                console.log('SSE 연결 설정, userId : ' + userId);
                //SSE 연결 설정
                eventSource = new EventSourcePolyfill(
                    `${import.meta.env.VITE_BASE_URL}/subscribe/${userId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 세션 스토리지 -> 로컬 스토리지
                            'Last-Event-ID': Date.now().toString()
                        },
                        withCredentials: true,
                        heartbeatTimeout: 45000,
                        connectionTimeout: 5000
                    }
                );

                // 연결 상태 관리
                eventSource.onopen = () => {
                    console.log('로그인 이후 -- SSE 연결 성공');
                    if (reconnectTimer) {
                        clearTimeout(reconnectTimer);
                        reconnectTimer = null;
                    }
                };

                // 하트비트 이벤트 처리
                eventSource.addEventListener('heartbeat', () => {
                    console.debug('SSE 연결 활성 상태 유지');
                });

                // 알림 이벤트 처리 - 'notification' 이벤트 수신 시..
                eventSource.addEventListener('notification', (e) => {
                    try {

                        console.log('notification 이벤트를 받았다!!');
                        console.log('e.data : ', e.data); // e.data 값 확인

                        const notification = JSON.parse(e.data);
                        console.log('받은 notification :: ' + notification);

                        if (Notification.permission === 'granted') {
                            console.log('Notifcation.permission이 granted이다 !');
                            console.log(notification.message);
                            const noti = new Notification('Movlit 알림', {
                                body: notification.message, // 알림 메세지 표시
                                icon: '/notification-icon.png'
                            });
                            // 테스트 위한 임시 alert
                            alert(notification.message);
                            noti.onclick = () => window.focus();
                            // 알림 종
                            setNotifications((prev) => [...prev, notification]);
                            setNewNotification(true); // 새로운 알림 발생
                        }
                    } catch (error) {
                        console.error('알림 처리 오류:', error);
                    }
                });

                // 오류 처리
                eventSource.onerror = (e) => {
                    console.error('SSE 연결 오류:', e);
                    if (eventSource) {
                        eventSource.close();
                        eventSource = null;
                    }
                    if (!reconnectTimer) {
                        reconnectTimer = setTimeout(setupSSE, 5000);
                    }
                };

            } catch (error) {
                console.error('SSE 초기화 실패:', error);
                if (!reconnectTimer) {
                    reconnectTimer = setTimeout(setupSSE, 10000);
                }
            }
        };

        if (isLoggedIn) setupSSE();

        return () => {
            if (eventSource) {
                eventSource.close();
                console.log('SSE 연결 종료');
            }
            if (reconnectTimer) clearTimeout(reconnectTimer);
        };
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
                    {/*{isLoggedIn && (*/}
                    {/*    <NavLink*/}
                    {/*        to="/chat" // 채팅 페이지로 이동하는 링크 추가*/}
                    {/*        className={({isActive}) => (isActive ? 'active' : '')}*/}
                    {/*    >*/}
                    {/*        채팅*/}
                    {/*    </NavLink>*/}
                    {/*)}*/}
                    <NavLink
                        to="/chatMain" // 채팅 페이지로 이동하는 링크 추가
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
                            <div onClick={handleBellClick} style={{ position: 'relative' }}>
                                <img src="/images/notification-bell-icon.png" alt="알림" className="noti-img"/>
                                {newNotification && <span className="badge">N</span>} {/* 빨간 점 표시 */}
                            </div>

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