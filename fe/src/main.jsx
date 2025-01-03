import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'; // 변경
import App from './App';
import MovieHome from './pages/MovieHome.jsx';
import BookHome from "./pages/BookHome.jsx";
import MemberRegister from './pages/MemberRegister';
import MemberLogin from './pages/MemberLogin';
import MovieDetailPage from './components/MovieDetailPage';

const router = createBrowserRouter([ // createBrowserRouter 사용
    {
        path: '/',
        element: <App/>,
        children: [
            {
                path: '/',
                element: <MovieHome/>,
            },
            {
                path: 'book', // book 경로 추가
                element: <BookHome />,
            },
            {
                path: 'member/register',
                element: <MemberRegister/>,
            },
            {
                path: 'member/login',
                element: <MemberLogin/>,
            },
            {
                path: 'movie/:movieId',
                element: <MovieDetailPage/>,
            },
            // BookDetail 라우팅용
            // {
            //     path: 'movie/:bookId',
            //     element: <BookDetailPage/>,
            // },
        ],
    },
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <RouterProvider router={router}/> {/* RouterProvider 사용 */}
    </StrictMode>
);