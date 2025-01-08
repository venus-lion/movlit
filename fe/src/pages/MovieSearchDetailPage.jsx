import { useEffect, useState } from "react";
import { Link } from 'react-router-dom';
import './SearchDetailPage.css'; // SearchPage.css 기반

function MovieSearchPage() {
    const [movieList, setMovieList] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await fetch(`/api/movies/main/popular?page=${page}&pageSize=9`); // 9개씩 가져오도록 pageSize 설정
                const data = await response.json();

                setMovieList(data.movieList); // movieList로 응답하도록 수정
                setTotalPages(data.totalPages); // totalPages 설정 (API 응답에 totalPages가 있다고 가정)

            } catch (error) {
                console.error('데이터 가져오기 실패:', error);
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [page]);

    const handlePageChange = (newPage) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setPage(newPage);
        }
    };

    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="search-body-detail">
            <div className="search-section-detail">
                <h2>영화</h2>
            </div>
            <div className="search-results-detail movies">
                {movieList.map((movie) => (
                    <div key={movie.movieId} className="search-item-detail">
                        <Link to={`/movie/${movie.movieId}`}>
                            <img src={movie.posterPath} alt={movie.title} />
                            <p>{movie.title}</p>
                        </Link>
                    </div>
                ))}
            </div>

            {loading && <div>Loading...</div>}

            <div className="pagination-detail">
                {Array.from({ length: totalPages }, (_, index) => index + 1).map((pageNum) => (
                    <button
                        key={pageNum}
                        className={page === pageNum ? "active" : ""}
                        onClick={() => handlePageChange(pageNum)}
                    >
                        {pageNum}
                    </button>
                ))}
            </div>
        </div>
    );
}

export default MovieSearchPage;