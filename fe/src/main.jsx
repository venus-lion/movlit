import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from './App';
import Home from './pages/Home';
import MemberRegister from './pages/MemberRegister';
import MemberLogin from './pages/MemberLogin';
import MovieDetailPage from './components/MovieDetailPage';

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App />}>
                    <Route index element={<Home />} />
                    <Route path="member/register" element={<MemberRegister />} />
                    <Route path="member/login" element={<MemberLogin />} />
                    <Route path="movie/:movieId" element={<MovieDetailPage />} />
                </Route>
            </Routes>
        </BrowserRouter>
    </StrictMode>
);