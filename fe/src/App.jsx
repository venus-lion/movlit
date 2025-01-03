import React, {useCallback, useState} from 'react';
import {NavLink, Outlet, useNavigate} from 'react-router-dom';
import axiosInstance from './axiosInstance';

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
            setIsLoggedIn(false);
            navigate('/member/login');
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    return (
        <>
            <nav id="nav">
                <ul>
                    <li>
                        <NavLink
                            to="/"
                            className={({isActive}) => (isActive ? 'current' : '')}
                        >
                            Home
                        </NavLink>
                    </li>
                    {!isLoggedIn && (
                        <>
                            <li>
                                <NavLink
                                    to="/member/register"
                                    className={({isActive}) => (isActive ? 'current' : '')}
                                >
                                    Register
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="/member/login"
                                    className={({isActive}) => (isActive ? 'current' : '')}
                                >
                                    Login
                                </NavLink>
                            </li>
                        </>
                    )}
                    {isLoggedIn && (
                        <li>
                            <button onClick={handleLogout} className="logout-button">
                                Logout
                            </button>
                        </li>
                    )}
                </ul>
            </nav>

            {/* Outlet에 context prop으로 updateLoginStatus 전달 */}
            <Outlet context={{updateLoginStatus}}/>
        </>
    );
}

export default App;