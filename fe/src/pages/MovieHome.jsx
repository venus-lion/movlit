import React, { useEffect, useState } from "react";
import MovieCarousel from "./MovieCarousel";
// import "../assets/css/main.css"; // 스타일 예시는 아래 참고
import "./MovieHome.css";
import "../assets/css/fontawesome-all.min.css";

// 예시용 가짜 데이터
const fakeThemeData = [
    {
        id: 1,
        name: "Movlit 인기 영화 목록",
        movies: [
            { id: 101, title: "오징어 게임 시즌2", imgUrl: "https://via.placeholder.com/150?text=오징어게임2" },
            { id: 102, title: "페라리", imgUrl: "https://via.placeholder.com/150?text=페라리" },
            { id: 103, title: "하얼빈", imgUrl: "https://via.placeholder.com/150?text=하얼빈" },
            { id: 104, title: "보그타: 마지막 기회의 땅", imgUrl: "https://via.placeholder.com/150?text=보그타" },
            { id: 105, title: "시빌 워: 분열의 시대", imgUrl: "https://via.placeholder.com/150?text=시빌워" },
            { id: 106, title: "6번째 영화", imgUrl: "https://via.placeholder.com/150?text=영화6" },
            { id: 107, title: "7번째 영화", imgUrl: "https://via.placeholder.com/150?text=영화7" },
        ],
    },
    {
        id: 2,
        name: "Movlit 최신 영화 목록",
        movies: [
            { id: 201, title: "슬램덩크", imgUrl: "https://via.placeholder.com/150?text=슬램덩크" },
            { id: 202, title: "Movie2", imgUrl: "https://via.placeholder.com/150?text=영화2" },
            { id: 203, title: "Movie3", imgUrl: "https://via.placeholder.com/150?text=영화3" },
            { id: 204, title: "Movie4", imgUrl: "https://via.placeholder.com/150?text=영화4" },
            { id: 205, title: "Movie5", imgUrl: "https://via.placeholder.com/150?text=영화5" },
            { id: 206, title: "Movie6", imgUrl: "https://via.placeholder.com/150?text=영화6" },
        ],
    },
    {
        id: 3,
        name: "액션",   // Genre_Id: 1
        movies: [
            { id: 301, title: "시리즈1", imgUrl: "https://via.placeholder.com/150?text=시리즈1" },
            { id: 302, title: "시리즈2", imgUrl: "https://via.placeholder.com/150?text=시리즈2" },
            { id: 303, title: "시리즈3", imgUrl: "https://via.placeholder.com/150?text=시리즈3" },
            { id: 304, title: "시리즈4", imgUrl: "https://via.placeholder.com/150?text=시리즈4" },
            { id: 305, title: "시리즈5", imgUrl: "https://via.placeholder.com/150?text=시리즈5" },
            { id: 306, title: "시리즈6", imgUrl: "https://via.placeholder.com/150?text=시리즈6" },
        ],
    },
    {
        id: 4,
        name: "애니메이션",  // Genre_Id: 2
        movies: [
            { id: 401, title: "시리즈7", imgUrl: "https://via.placeholder.com/150?text=시리즈7" },
            { id: 402, title: "시리즈8", imgUrl: "https://via.placeholder.com/150?text=시리즈8" },
            { id: 403, title: "시리즈9", imgUrl: "https://via.placeholder.com/150?text=시리즈9" },
            { id: 404, title: "시리즈10", imgUrl: "https://via.placeholder.com/150?text=시리즈10" },
            { id: 405, title: "시리즈11", imgUrl: "https://via.placeholder.com/150?text=시리즈11" },
            { id: 406, title: "시리즈12", imgUrl: "https://via.placeholder.com/150?text=시리즈12" },
        ],
    },
    {
        id: 5,
        name: "코미디",    // Genre_Id: 3
        movies: [
            { id: 501, title: "추가1", imgUrl: "https://via.placeholder.com/150?text=추가1" },
            { id: 502, title: "추가2", imgUrl: "https://via.placeholder.com/150?text=추가2" },
            { id: 503, title: "추가3", imgUrl: "https://via.placeholder.com/150?text=추가3" },
            { id: 504, title: "추가4", imgUrl: "https://via.placeholder.com/150?text=추가4" },
            { id: 505, title: "추가5", imgUrl: "https://via.placeholder.com/150?text=추가5" },
            { id: 506, title: "추가6", imgUrl: "https://via.placeholder.com/150?text=추가6" },
        ],
    },
    {
        id: 6,
        name: "범죄",     // Genre_Id: 4
        movies: [
            { id: 601, title: "추가7", imgUrl: "https://via.placeholder.com/150?text=추가7" },
            { id: 602, title: "추가8", imgUrl: "https://via.placeholder.com/150?text=추가8" },
            { id: 603, title: "추가9", imgUrl: "https://via.placeholder.com/150?text=추가9" },
            { id: 604, title: "추가10", imgUrl: "https://via.placeholder.com/150?text=추가10" },
            { id: 605, title: "추가11", imgUrl: "https://via.placeholder.com/150?text=추가11" },
            { id: 606, title: "추가12", imgUrl: "https://via.placeholder.com/150?text=추가12" },
        ],
    },
    {
        id: 7,
        name: "다큐멘터리",  // Genre_Id: 5
        movies: [
            { id: 701, title: "추가13", imgUrl: "https://via.placeholder.com/150?text=추가13" },
            { id: 702, title: "추가14", imgUrl: "https://via.placeholder.com/150?text=추가14" },
            { id: 703, title: "추가15", imgUrl: "https://via.placeholder.com/150?text=추가15" },
            { id: 704, title: "추가16", imgUrl: "https://via.placeholder.com/150?text=추가16" },
            { id: 705, title: "추가17", imgUrl: "https://via.placeholder.com/150?text=추가17" },
            { id: 706, title: "추가18", imgUrl: "https://via.placeholder.com/150?text=추가18" },
        ],
    },
];

function App() {
    // 처음에는 테마 데이터의 앞 4개만 로딩
    const [themeList, setThemeList] = useState(fakeThemeData.slice(0, 4));
    const [isAllLoaded, setIsAllLoaded] = useState(false);

    // 스크롤 이벤트로 추가 3개 테마 로딩(한 번만)
    useEffect(() => {
        const handleScroll = () => {
            const scrollHeight = document.documentElement.scrollHeight;
            const scrollTop = document.documentElement.scrollTop;
            const clientHeight = document.documentElement.clientHeight;

            // 바닥 부근까지 스크롤이 내려갔으면
            if (!isAllLoaded && scrollTop + clientHeight + 200 >= scrollHeight) {
                // 추가 3개 로드
                setThemeList((prev) => [...prev, ...fakeThemeData.slice(4, 7)]);
                setIsAllLoaded(true);
            }
        };

        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, [isAllLoaded]);

    return (
        <div className="app-container">
            <header className="header">
                <nav className="nav">
                    <h1>WATCHA PEDIA</h1>
                    <ul>
                        <li>영화</li>
                        <li>시리즈</li>
                        <li>책</li>
                        <li>웹툰</li>
                    </ul>
                </nav>
            </header>

            {/* 테마별 MovieCarousel 표시 */}
            {themeList.map((theme) => (
                <MovieCarousel key={theme.id} theme={theme} />
            ))}

            {/* 모든 테마가 로딩된 후에도 더 아래로 스크롤하면... */}
            {isAllLoaded && (
                <div style={{ textAlign: "center", margin: "40px" }}>
                    <p>더 이상 불러올 테마가 없습니다.</p>
                </div>
            )}
        </div>
    );
}

export default App;
