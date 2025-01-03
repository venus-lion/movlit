import React, { useState } from "react";
// import "../assets/css/main.css"; // 스타일 예시는 아래 참고
import "./MovieCarousel.css";
import "../assets/css/fontawesome-all.min.css";

function MovieCarousel({ theme }) {
    const [page, setPage] = useState(0);
    const itemsPerPage = 5;

    // 전체 영화 개수
    const totalMovies = theme.movies.length;
    // 총 페이지 수(= 슬라이드 수)
    const totalPages = Math.ceil(totalMovies / itemsPerPage);

    // 현재 페이지에 보여줄 영화들
    const startIndex = page * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const visibleMovies = theme.movies.slice(startIndex, endIndex);

    // 화살표 버튼 핸들러
    const handleNext = () => {
        if (page < totalPages - 1) {
            setPage(page + 1);
        }
    };

    const handlePrev = () => {
        if (page > 0) {
            setPage(page - 1);
        }
    };

    return (
        <div className="carousel-wrapper">
            <h2 className="theme-title">{theme.name}</h2>
            <div className="carousel-container">
                {page > 0 && (
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

                {page < totalPages - 1 && (
                    <button className="arrow-button right" onClick={handleNext}>
                        &gt;
                    </button>
                )}
            </div>
        </div>
    );
}

export default MovieCarousel;
