// src/components/LatestMovies.jsx
import React, {useState} from 'react';
import useMovieList from '../hooks/useMovieList.jsx';
import MovieCarousel from './MovieCarousel.jsx';

function LatestMovies() {
    const { movies, loadMore, loading, error, hasMore } = useMovieList({
        endpoint: '/api/movies/main/latest',
        params: { pageSize: 20 },
    });

    const [startIndex, setStartIndex] = useState(0);

    const handleNext = () => {
        const newIndex = startIndex + 5;
        if (newIndex + 5 > movies.length && hasMore && !loading) {
            loadMore();
        }
        setStartIndex(newIndex);
    };

    const handlePrev = () => {
        const newIndex = startIndex - 5;
        if (newIndex >= 0) {
            setStartIndex(newIndex);
        }
    };

    if (loading && movies.length === 0) return <p>Loading latest popular movies...</p>;
    if (error) return (
        <div>
            <p>Error loading latest popular movies.</p>
            <button onClick={loadMore}>Retry</button>
        </div>
    );

    return (
        <MovieCarousel
            title="최신 인기 영화"
            movies={movies}
            startIndex={startIndex}
            handleNext={handleNext}
            handlePrev={handlePrev}
            hasMore={hasMore}
            loading={loading}
        />
    );
}

export default LatestMovies;