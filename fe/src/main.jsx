import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import './index.css';
import App from './App.jsx';
import MemberLogin from './components/MemberLogin';
import MemberRegister from './components/MemberRegister';
import Home from './Home';

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route index element={<Home/>}/>
                    <Route path="member/register" element={<MemberRegister/>}/>
                    <Route path="member/login" element={<MemberLogin/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    </StrictMode>
);