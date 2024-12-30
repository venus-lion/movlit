import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';

function MovieDetailPage() {
    const {movieId} = useParams();
    const [movieData, setMovieData] = useState(null);
    const [myRating, setMyRating] = useState(0);

    useEffect(() => {
        fetch(`/api/movies/${movieId}/detail`)
            .then((response) => response.json())
            .then((data) => {
                setMovieData({
                    id: data.movieId,
                    title: data.title,
                    originalTitle: data.originalTitle,
                    overview: data.overview,
                    popularity: data.popularity,
                    heartCount: data.heartCount,
                    posterUrl: "http://image.tmdb.org/t/p/w200" + data.posterPath,
                    backdropUrl: data.backdropPath,
                    releaseDate: data.releaseDate,
                    country: data.productionCountry,
                    language: data.originalLanguage,
                    runtime: data.runtime,
                    status: data.status,
                    voteCount: data.voteCount,
                    tagline: data.tagline,
                    ratingCount: data.voteCount,
                });
            })
            .catch((error) => console.error('Error fetching movie data:', error));
    }, [movieId]);

    const handleRatingChange = (newRating) => {
        setMyRating(newRating);
        // 여기에 별점 저장 로직 추가 (예: API 호출)
    };

    if (!movieData) {
        return <div style={styles.loading}>Loading...</div>;
    }

    return (
        <div style={styles.container}>
            <div
                style={{
                    ...styles.header,
                    backgroundImage: `url(http://image.tmdb.org/t/p/original${movieData.backdropUrl})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    color: 'white', // 텍스트 색상
                }}
            >
                <div style={styles.breadcrumbs}>
                    홈 / 영화 / {movieData.title}
                </div>
                <div style={styles.title}>{movieData.title}</div>
                <div style={styles.subtitle}>
                    {movieData.releaseDate ? movieData.releaseDate.substring(0, 4) : ''}
                    {' '}・ {movieData.genre} ・ {movieData.country}
                </div>
            </div>

            {/* 나머지 부분은 이전과 동일 */}
            <div style={styles.mainContent}>
                <div style={styles.poster}>
                    <img src={movieData.posterUrl} alt={movieData.title}/>
                </div>

                <div style={styles.info}>
                    <div style={styles.myRating}>
                        <span style={styles.ratingLabel}>내 별점</span>
                        <div style={styles.stars}>
                            {[...Array(5)].map((_, index) => (
                                <span
                                    key={index}
                                    style={index < myRating ? styles.starFilled : styles.starEmpty}
                                    onClick={() => handleRatingChange(index + 1)}
                                >
                                    ★
                                </span>
                            ))}
                        </div>
                    </div>

                    <div style={styles.buttonGroup}>
                        <button style={styles.button}>찜</button>
                    </div>

                    <div style={styles.details}>
                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>줄거리</div>
                            <div style={styles.sectionContent}>{movieData.overview}</div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>출연/제작</div>
                            <div style={styles.sectionContent}>
                                {movieData.cast &&
                                    movieData.cast.map((actor) => (
                                        <div key={actor.id}>{actor.name}</div>
                                    ))}
                            </div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>평점/리뷰</div>
                            <div style={styles.sectionContent}>
                                {movieData.reviews &&
                                    movieData.reviews.map((review) => (
                                        <div key={review.id}>{review.content}</div>
                                    ))}
                            </div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>관련 도서</div>
                            <div style={styles.sectionContent}>
                                {movieData.relatedBooks &&
                                    movieData.relatedBooks.map((book) => (
                                        <div key={book.id} style={styles.book}>
                                            <img src={book.coverUrl} alt={book.title}/>
                                            <div>{book.title}</div>
                                        </div>
                                    ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

const styles = {
    container: {
        width: '100%',
        fontFamily: 'Arial, sans-serif',
    },
    header: {
        padding: '210px 20px', // 위아래 패딩을 60px로 조정 (원하는 값으로 수정)
        borderBottom: '1px solid #e7e7e7',
        marginBottom: '20px',
    },
    breadcrumbs: {
        marginBottom: '10px',
        fontSize: '14px',
        color: '#ffffff',
    },
    title: {
        fontSize: '28px',
        fontWeight: 'bold',
        marginBottom: '5px',
        color: '#ffffff',
    },
    subtitle: {
        fontSize: '16px',
        color: '#ffffff',
    },
    mainContent: {
        display: 'flex',
        padding: '20px',
    },
    poster: {
        width: '200px',
        marginRight: '30px',
        flexShrink: 0,
    },
    info: {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
    },
    myRating: {
        marginBottom: '20px',
    },
    stars: {
        display: 'inline-block',
        marginLeft: '10px',
    },
    starFilled: {
        color: '#f8d90f',
        cursor: 'pointer',
    },
    starEmpty: {
        color: '#ccc',
        cursor: 'pointer',
    },
    buttonGroup: {
        display: 'flex',
        marginBottom: '20px',
    },
    button: {
        marginRight: '10px',
        padding: '10px 20px',
        backgroundColor: '#4080ff',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    details: {
        marginTop: 'auto',
    },
    section: {
        marginBottom: '20px',
    },
    sectionTitle: {
        fontSize: '20px',
        fontWeight: 'bold',
        marginBottom: '10px',
        color: '#000000'
    },
    sectionContent: {
        color: '#000000'
    },
    book: {
        display: 'flex',
        alignItems: 'center',
        marginBottom: '10px',
    },
    loading: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
    },
};

export default MovieDetailPage;