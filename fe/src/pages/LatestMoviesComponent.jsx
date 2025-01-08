import React, { useState } from 'react';
import useMovieList from '../hooks/useMovieList';
import MovieCarousel from './MovieCarousel';

function LatestMoviesComponent() {
    const { movies, loading, error } = useMovieList({
        endpoint: '/api/movies/main/latest',
        params: { pageSize: 20 },
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

    if (loading) return <p>Loading latest movies...</p>;
    if (error) return (
        <div>
            <p>Error loading latest movies.</p>
        </div>
    );

    return (
        <MovieCarousel
            title="최신 인기 영화"
            movies={movies}
            startIndex={startIndex}
            handleNext={handleNext}
            handlePrev={handlePrev}
        />
    );
}

export default LatestMoviesComponent;
