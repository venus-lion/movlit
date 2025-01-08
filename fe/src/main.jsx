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
import MyPage from './components/MyPage';
import MemberUpdate from './pages/MemberUpdate'; // MemberUpdate import
import SearchPage from "./pages/SearchPage.jsx";
import BookSearchDetailPage from "./pages/BookSearchDetailPage.jsx";
//
import MovieSearchDetailPage from "./pages/MovieSearchDetailPage.jsx";

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
                path: 'mypage',
                element: <MyPage />,
            },
            {
                path: 'member/update', // 회원 수정 라우트 추가
                element: <MemberUpdate />,
            },
            {
                path: 'search/:inputStr', // 검색기능 라우트 추가
                element: <SearchPage />
            },
            {
                path: 'movies/search/:inputStr',
                element: <MovieSearchDetailPage />
            },
            {
                path: 'books/search/:inputStr', // 도서 더 보기 라우트 추가
                element: <BookSearchDetailPage />,
            },
        ],
    },
]);

createRoot(document.getElementById('root')).render(
    // <StrictMode>
        <RouterProvider router={router} />
    // </StrictMode>
);