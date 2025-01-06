// src/components/PopularMovies.js
import React from 'react';
import useMovieList from '../hooks/useMovieList.js';
import MovieCarousel from './MovieCarousel';

function PopularMovies() {
    const { movies, loadMore, loading, error, hasMore } = useMovieList({
        endpoint: '/api/movies/main/popular',
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

    // 필요한 경우 startIndex 초기화
    useEffect(() => {
        // 예를 들어, 새로운 데이터를 불러올 때 startIndex를 유지하거나 초기화할 수 있음
    }, [movies]);

    if (loading && movies.length === 0) return <p>Loading popular movies...</p>;
    if (error) return (
        <div>
            <p>Error loading popular movies.</p>
            <button onClick={loadMore}>Retry</button>
        </div>
    );

    return (
        <MovieCarousel
            title="인기 많은 영화"
            movies={movies}
            startIndex={startIndex}
            handleNext={handleNext}
            handlePrev={handlePrev}
            hasMore={hasMore}
            loading={loading}
        />
    );
}

export default PopularMovies;