import React, { useEffect, useState } from "react";
import axios from "axios";
// import "../assets/css/main.css"; // 스타일 예시는 아래 참고
import "./MovieCarousel.css";
import "../assets/css/fontawesome-all.min.css";

/**
 * props 예시:
 *  - name: 화면에 표시할 목록 이름 (ex: "인기순 영화")
 *  - endpoint: API URL (ex: "/api/movies/main/popular")
 *  - defaultParams: { genreId: 1 } 등 기본 query params
 *    (최신순 등에서 필요 없으면 {} 로 전달)
 */
function MovieCarousel({ name, endpoint, defaultParams = {} }) {
    // 서버에서 누적받은 영화들 (각 호출마다 10개씩 추가됨)
    const [movies, setMovies] = useState([]);

    // API 호출에 쓰일 page 파라미터 (1부터 시작)
    const [apiPage, setApiPage] = useState(1);

    // 화면에서 현재 몇 번째 5개 슬라이드(0부터)
    const [uiPage, setUiPage] = useState(0);

    // 한 번에 보여줄 영화 수
    const itemsPerPage = 5;

    // 컴포넌트 첫 로딩 시, page=1로 API 불러오기
    useEffect(() => {
        fetchMovies(1);
        // eslint-disable-next-line
    }, []);

    // 서버에서 10개씩 받아오는 함수
    const fetchMovies = async (pageToFetch) => {
        try {
            const response = await axios.get(endpoint, {
                params: {
                    ...defaultParams,
                    page: pageToFetch,
                    pageSize: 10
                },
            });
            // 서버가 10개짜리 배열을 내려준다고 가정
            const newData = response.data;
            console.log(newData);
            // 누적
            setMovies((prev) => [...prev, ...newData]);
            // apiPage 갱신
            setApiPage(pageToFetch);
        } catch (err) {
            console.error("API 에러:", err);
        }
    };

    // 현재 UI에 보여줄 영화 5개
    const startIndex = uiPage * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const visibleMovies = movies.slice(startIndex, endIndex);

    // 오른쪽 화살표 핸들러
    const handleNext = () => {
        const nextUiPage = uiPage + 1;
        const nextStartIndex = nextUiPage * itemsPerPage;

        // 만약 로컬에 nextUiPage가 보여줄 5개가 충분치 않으면 (즉 nextStartIndex가 현재 movies 범위 밖)
        if (nextStartIndex >= movies.length) {
            // 다음 서버 page 호출 (예: apiPage=1 이었다면 2)
            const nextApiPage = apiPage + 1;
            fetchMovies(nextApiPage);
        }

        setUiPage(nextUiPage);
    };

    // 왼쪽 화살표 핸들러
    const handlePrev = () => {
        // uiPage가 0이면 더 이상 왼쪽으로 갈 수 없음
        if (uiPage > 0) {
            setUiPage(uiPage - 1);
        }
    };

    // 왼쪽 화살표 버튼을 보일지 여부
    const showLeftArrow = uiPage > 0;

    return (
        <div className="carousel-wrapper">
            <h2 className="theme-title">{name}</h2>
            <div className="carousel-container">
                {showLeftArrow && (
                    <button className="arrow-button left" onClick={handlePrev}>
                        &lt;
                    </button>
                )}

                <div className="carousel-content">
                    {visibleMovies.map((movie) => (
                        <div className="movie-card" key={movie.id}>
                            <img src={movie.imgUrl} alt={movie.title} />
                            <p>{movie.title}</p>
                        </div>
                    ))}
                </div>

                <button className="arrow-button right" onClick={handleNext}>
                    &gt;
                </button>
            </div>
        </div>
    );
}

export default MovieCarousel;