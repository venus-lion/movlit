import React, {useCallback, useState} from 'react';
import {Link, Outlet, useNavigate} from 'react-router-dom';
import axios from 'axios';

function App() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(!!sessionStorage.getItem('accessToken'));

    const updateLoginStatus = useCallback((status) => {
        setIsLoggedIn(status);
    }, []);

    const handleLogout = async () => {
        try {
            const accessToken = sessionStorage.getItem('accessToken');
            await axios.post('/api/members/logout', null, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            sessionStorage.removeItem('accessToken');
            document.cookie = 'refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
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
                    <li className="current">
                        <Link to="/">Home</Link>
                    </li>
                    {!isLoggedIn && (
                        <>
                            <li>
                                <Link to="/member/register">Register</Link>
                            </li>
                            <li>
                                <Link to="/member/login">Login</Link>
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
            <Outlet context={{isLoggedIn, updateLoginStatus}}/>
        </>
    );
}

export default App;