// src/hooks/useMovieList.js
import { useEffect, useState, useMemo } from 'react';
import axios from 'axios';

const useMovieList = ({ endpoint, params = {}, initialPage = 1, pageSize = 20 }) => {
    const [movies, setMovies] = useState([]);
    const [currentPage, setCurrentPage] = useState(initialPage);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [hasMore, setHasMore] = useState(true);

    const fetchMovies = useCallback(async (page) => {
        try {
            setLoading(true);
            const response = await axios.get(endpoint, {
                params: { ...params, page, pageSize },
            });
            const newMovies = response.data.movieList;
            setMovies(prevMovies => [...prevMovies, ...newMovies]);
            setHasMore(newMovies.length === pageSize); // 다음 페이지가 있는지 확인
        } catch (err) {
            console.error(`Error fetching movies from ${endpoint}:`, err);
            setError(err);
        } finally {
            setLoading(false);
        }
    }, [endpoint, params, pageSize]);

    useEffect(() => {
        fetchMovies(currentPage);
    }, [fetchMovies, currentPage]);

    const loadMore = () => {
        if (!loading && hasMore) {
            setCurrentPage(prevPage => prevPage + 1);
        }
    };

    // const handleNext = () => {
    //     if (startIndex + 5 < movies.length) {
    //         setStartIndex(startIndex + 5);
    //     }
    // };
    //
    // const handlePrev = () => {
    //     if (startIndex > 0) {
    //         setStartIndex(startIndex - 5);
    //     }
    // };

    return { movies, loadMore, loading, error, hasMore };
};

export default useMovieList;
