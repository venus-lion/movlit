import React, { useState } from 'react';
import MovieCarousel from './MovieCarousel';
import useGenreMovieList from "../hooks/useGenreMovieList.jsx";

function GenreMoviesComponent({ genreId }) {
    const { movies, loading, error, genreName } = useGenreMovieList({
        endpoint: '/api/movies/main/genre',
        params: { genreId: genreId, pageSize: 50 },
    });

    const [startIndex, setStartIndex] = useState(0);  // 화면에 보이는 영화 시작 인덱스

    const handleNext = () => {
        const newIndex = startIndex + 5;
        if (newIndex < movies.length) {
            setStartIndex(newIndex);
        }
    };

    const handlePrev = () => {
        const newIndex = startIndex - 5;
        if (newIndex >= 0) {
            setStartIndex(newIndex);
        }
    };

    if (loading) return <p>Loading movies...</p>;
    if (error) return (
        <div>
            <p>Error loading movies.</p>
        </div>
    );

    return (
        <MovieCarousel
            title={`${genreName} 영화`}  // genreId에 따라 제목 변경 가능
            movies={movies}
            startIndex={startIndex}
            handleNext={handleNext}
            handlePrev={handlePrev}
        />
    );
}

export default GenreMoviesComponent;
