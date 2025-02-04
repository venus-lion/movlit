import React, {useContext} from 'react';
import {Navigate} from 'react-router-dom';
import {AppContext} from './App';

function PrivateRoute({children}) {
    const {isLoggedIn} = useContext(AppContext);

    if (!isLoggedIn) {
        // 로그인하지 않은 경우 로그인 페이지로 리디렉션
        return <Navigate to="/member/login" replace/>;
    }

    // 로그인한 경우 children 렌더링 (이 경우 MyPage)
    return children;
}

export default PrivateRoute;