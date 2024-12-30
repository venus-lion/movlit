import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

function Home() {
    const [trendingMovies, setTrendingMovies] = useState([]);
    const [boxOfficeMovies, setBoxOfficeMovies] = useState([]);

    useEffect(() => {
        // Trending 영화 데이터 가져오기 (예시)
        fetch('/api/movies/trending')
            .then((response) => response.json())
            .then((data) => setTrendingMovies(data))
            .catch((error) => console.error('Error fetching trending movies:', error));

        // 박스 오피스 데이터 가져오기 (예시)
        fetch('/api/movies/boxoffice')
            .then((response) => response.json())
            .then((data) => setBoxOfficeMovies(data))
            .catch((error) => console.error('Error fetching box office movies:', error));
    }, []);

    return (
        <div style={styles.container}>
            <div style={styles.topBanner}>
                {/* 이 부분에 크게 공지사항, 이벤트 등의 배너 추가 */}
            </div>

            <div style={styles.content}>
                <div style={styles.section}>
                    <div style={styles.sectionHeader}>
                        <h2 style={styles.sectionTitle}>요즘 뜨는 영화</h2>
                        <Link to="/movies/trending" style={styles.moreLink}>더보기</Link>
                    </div>
                    <div style={styles.movieList}>
                        {trendingMovies.map((movie) => (
                            <Link to={`/movie/${movie.id}`} key={movie.id} style={styles.movie}>
                                {/* 포스터 이미지 (movie.posterPath가 URL 형식이라고 가정) */}
                                <img src={movie.posterPath} alt={movie.title} style={styles.poster}/>
                                <div style={styles.movieInfo}>
                                    <div style={styles.movieTitle}>{movie.title}</div>
                                    {/* 평점 정보가 있다면 표시 (movie.voteAverage가 0~10 사이라고 가정) */}
                                    {movie.voteAverage && (
                                        <div style={styles.movieRating}>★ {(movie.voteAverage / 2).toFixed(1)}</div>
                                    )}
                                </div>
                            </Link>
                        ))}
                    </div>
                </div>

                <div style={styles.section}>
                    <div style={styles.sectionHeader}>
                        <h2 style={styles.sectionTitle}>일간 박스 오피스</h2>
                        <Link to="/movies/boxoffice" style={styles.moreLink}>더보기</Link>
                    </div>
                    <div style={styles.boxOfficeList}>
                        {boxOfficeMovies.map((movie) => (
                            <Link to={`/movie/${movie.id}`} key={movie.id} style={styles.boxOfficeMovie}>
                                {/* 포스터 이미지 (movie.posterPath가 URL 형식이라고 가정) */}
                                <img src={movie.posterPath} alt={movie.title} style={styles.boxOfficePoster}/>
                                <div style={styles.boxOfficeRank}>{movie.rank}</div>
                                {/* 순위 표시 */}
                            </Link>
                        ))}
                    </div>
                </div>

                {/* 다른 섹션들 (예: TV 프로그램, 책)도 유사하게 추가 */}
            </div>
        </div>
    );
}

const styles = {
    container: {
        fontFamily: 'Arial, sans-serif',
    },
    topBanner: {
        // 상단 배너 스타일
        backgroundColor: '#f0f0f0',
        padding: '20px',
        textAlign: 'center',
        marginBottom: '20px',
    },
    content: {
        padding: '0 20px',
    },
    section: {
        marginBottom: '40px',
    },
    sectionHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '20px',
    },
    sectionTitle: {
        fontSize: '24px',
        fontWeight: 'bold',
    },
    moreLink: {
        fontSize: '16px',
        color: '#0077cc',
    },
    movieList: {
        display: 'flex',
        overflowX: 'auto',
    },
    movie: {
        marginRight: '15px',
        textDecoration: 'none',
        color: 'inherit',
    },
    poster: {
        width: '150px',
        height: '225px',
        objectFit: 'cover',
        marginBottom: '10px',
    },
    movieInfo: {
        textAlign: 'center',
    },
    movieTitle: {
        fontSize: '16px',
        fontWeight: 'bold',
    },
    movieRating: {
        fontSize: '14px',
        color: '#666',
    },
    boxOfficeList: {
        display: 'flex',
        overflowX: 'auto',
    },
    boxOfficeMovie: {
        position: 'relative',
        marginRight: '15px',
        textDecoration: 'none',
        color: 'inherit',
    },
    boxOfficePoster: {
        width: '100px',
        height: '150px',
        objectFit: 'cover',
    },
    boxOfficeRank: {
        position: 'absolute',
        top: '0',
        left: '0',
        backgroundColor: 'rgba(0, 0, 0, 0.7)',
        color: 'white',
        padding: '5px',
        fontWeight: 'bold',
    },
};

export default Home;