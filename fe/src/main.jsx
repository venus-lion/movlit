import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App';

import MovieHome from './pages/MovieHome.jsx';
import BookHome from './pages/BookHome.jsx';
import MemberRegister from './pages/MemberRegister';
import MemberLogin from './pages/MemberLogin';
import MovieDetailPage from './components/MovieDetailPage';
import BookDetailPage from './components/BookDetailPage';
import MyPage from './components/MyPage'; // MyPage import

const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        children: [
            {
                path: '/',
                element: <MovieHome />,
            },
            {
                path: 'book',
                element: <BookHome />,
            },
            {
                path: 'member/register',
                element: <MemberRegister />,
            },
            {
                path: 'member/login',
                element: <MemberLogin />,
            },
            {
                path: 'movie/:movieId',
                element: <MovieDetailPage />,
            },
            {
                path: 'book/:bookId',
                element: <BookDetailPage />,
            },
            {
                path: 'mypage', // MyPage 라우트 추가
                element: <MyPage />,
            },
        ],
    },
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <RouterProvider router={router} />
    </StrictMode>
);