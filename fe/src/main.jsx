import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'; // 변경
import App from './App';
import Home from './pages/MovieHome';
import MemberRegister from './pages/MemberRegister';
import MemberLogin from './pages/MemberLogin';
import MovieDetailPage from './components/MovieDetailPage';
import BookDetailPage from "./components/BookDetailPage";

const router = createBrowserRouter([ // createBrowserRouter 사용
    {
        path: '/',
        element: <App/>,
        children: [
            {
                index: true,
                element: <Home/>,
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
            {
                path: 'book/:bookId',
                element: <BookDetailPage/>,
            },
        ],
    },
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <RouterProvider router={router}/> {/* RouterProvider 사용 */}
    </StrictMode>
);