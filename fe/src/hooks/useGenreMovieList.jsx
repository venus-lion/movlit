import { useEffect, useState } from 'react';
import axios from 'axios';

const useGenreMovieList = ({ endpoint, params = {}, pageSize = 20 }) => {
    const [movies, setMovies] = useState([]);  // 전체 영화 목록
    const [genreId, setGenreId] = useState("");  // 영화 장르 ID
    const [genreName, setGenreName] = useState("");  // 영화 장르 이름
    const [loading, setLoading] = useState(true);  // 로딩 상태
    const [error, setError] = useState(null);  // 오류 상태

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const response = await axios.get(endpoint, {
                    params: { ...params, pageSize },
                });
                setMovies(response.data.movieList);  // 영화 목록 저장
                setGenreId(response.data.genreId);
                setGenreName(response.data.genreName);

            } catch (err) {
                console.error(`Error fetching movies from ${endpoint}:`, err);
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchMovies();  // 컴포넌트가 마운트될 때 API 호출
    }, []);

    return { movies, genreId, genreName, loading, error };
};

export default useGenreMovieList;
