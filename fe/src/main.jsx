import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'; // 변경
import App from './App';
<<<<<<< HEAD
import MovieHome from './pages/MovieHome.jsx';
import BookHome from "./pages/BookHome.jsx";
=======
import Home from './pages/MovieHome';
>>>>>>> a2aea4e61d66ddd9cbee96d52b4e46dd7cf48400
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
<<<<<<< HEAD
            // BookDetail 라우팅용
            // {
            //     path: 'movie/:bookId',
            //     element: <BookDetailPage/>,
            // },
=======
            {
                path: 'book/:bookId',
                element: <BookDetailPage/>,
            },
>>>>>>> a2aea4e61d66ddd9cbee96d52b4e46dd7cf48400
        ],
    },
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <RouterProvider router={router}/> {/* RouterProvider 사용 */}
    </StrictMode>
);