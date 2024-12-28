import React, {useState} from 'react';
import {Link, Outlet} from 'react-router-dom';

// 기존 App.jsx의 내용 (Vite + React 로고 및 카운터)을
// 별도의 컴포넌트로 분리
function Home() {
    const [count, setCount] = useState(0);

    return (
        <>
            <div>
                <a href="https://vitejs.dev" target="_blank" rel="noreferrer">
                    <img src="/vite.svg" className="logo" alt="Vite logo"/>
                </a>
                <a href="https://react.dev" target="_blank" rel="noreferrer">
                    <img src="/react.svg" className="logo react" alt="React logo"/>
                </a>
            </div>
            <h1>Vite + React</h1>
            <div className="card">
                <button onClick={() => setCount((count) => count + 1)}>
                    count is {count}
                </button>
                <p>
                    Edit <code>src/App.jsx</code> and save to test HMR
                </p>
            </div>
            <p className="read-the-docs">
                Click on the Vite and React logos to learn more
            </p>
        </>
    );
}

function App() {
    return (
        <div>
            {/* 네비게이션 메뉴 */}
            <nav>
                <ul>
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    <li>
                        <Link to="/member/list">Member List</Link>
                    </li>
                    <li>
                        <Link to="/member/register">Register</Link>
                    </li>
                    <li>
                        <Link to="/member/login">Login</Link>
                    </li>
                </ul>
            </nav>
            <Outlet/>
        </div>
    );
}

export default App;