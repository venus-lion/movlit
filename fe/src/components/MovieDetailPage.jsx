import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import axiosInstance from '../axiosInstance'; // axiosInstance 임포트

function MovieDetailPage() {
    const {movieId} = useParams();
    const [movieData, setMovieData] = useState(null);
    const [myRating, setMyRating] = useState(0);
    const [crews, setCrews] = useState([]);
    const [genres, setGenres] = useState([]);
    const [isWish, setIsWish] = useState(false);
    const [showCommentInput, setShowCommentInput] = useState(false);
    const [comment, setComment] = useState('');

    useEffect(() => {
        axiosInstance
            .get(`/movies/${movieId}/detail`)
            .then((response) => {
                const data = response.data;
                setMovieData({
                    id: data.movieId,
                    title: data.title,
                    originalTitle: data.originalTitle,
                    overview: data.overview,
                    popularity: data.popularity,
                    heartCount: data.heartCount,
                    posterUrl: 'http://image.tmdb.org/t/p/w200' + data.posterPath,
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

        axiosInstance
            .get(`/movies/${movieId}/crews`)
            .then((response) => setCrews(response.data))
            .catch((error) => console.error('Error fetching crew data:', error));

        axiosInstance
            .get(`/movies/${movieId}/genres`)
            .then((response) => {
                // response.data를 가공하여 name 속성을 가진 객체들의 배열로 변환
                const formattedGenres = response.data.map((genre) => ({
                    name: genre.genreName,
                }));
                setGenres(formattedGenres);
            })
            .catch((error) => console.error('Error fetching genre data', error));
    }, [movieId]);

    const handleRatingChange = (newRating) => {
        if (myRating === newRating) {
            setMyRating(0);
            setShowCommentInput(false);
        } else {
            setMyRating(newRating);
            setShowCommentInput(true);
        }
    };

    const handleWishClick = () => {
        setIsWish(!isWish);
        // 여기에 찜하기/찜해제 로직 추가 (예: API 호출)
    };

    const handleCommentChange = (event) => {
        setComment(event.target.value);
    };

    const handleSubmitComment = () => {
        if (myRating === 0) {
            alert('별점을 입력해주세요.');
            return;
        }
        if (comment.trim() === '') {
            alert('코멘트를 입력해주세요.');
            return;
        }

        const requestBody = {
            score: myRating,
            comment: comment,
        };

        axiosInstance
            .post(`/movies/${movieId}/comments`, requestBody)
            .then((response) => {
                console.log('코멘트 저장 성공:', response.data);
                alert('코멘트가 저장되었습니다.');
            })
            .catch((error) => {
                console.error('코멘트 저장 실패:', error);
                alert('코멘트 저장에 실패했습니다.');
            })
            .finally(() => {
                setComment('');
                setMyRating(0);
                setShowCommentInput(false);
            });
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
                    color: 'white',
                }}
            >
                <div style={styles.breadcrumbs}>홈 / 영화 / {movieData.title}</div>
                <div style={styles.title}>{movieData.title}</div>
                <div style={styles.subtitle}>
                    {movieData.releaseDate ? movieData.releaseDate.substring(0, 4) : ''}
                    {' ・ '}
                    {/* 장르 목록 출력 */}
                    {genres.map((genre, index) => (
                        <span key={index}>{genre.name}
                            {/* 마지막 장르 뒤에는 쉼표를 붙이지 않음 */}
                            {index < genres.length - 1 ? ', ' : ''}
                        </span>
                    ))}
                    {' ・ '}
                    {movieData.country}
                </div>
            </div>

            <div style={styles.mainContent}>
                <div style={styles.poster}>
                    <img src={movieData.posterUrl} alt={movieData.title}/>
                </div>

                <div style={styles.info}>
                    <div style={styles.ratingAndWish}>
                        <div style={styles.myRating}>
                            <span style={styles.ratingLabel}>내 별점</span>
                            <div style={styles.stars}>
                                {[...Array(5)].map((_, index) => (
                                    <span
                                        key={index}
                                        style={index < myRating ? styles.starFilled : styles.starEmpty}
                                        onClick={() => handleRatingChange(index + 1)}
                                    >
                    <span style={styles.starIcon}>★</span>
                  </span>
                                ))}
                            </div>
                        </div>
                        <div style={styles.buttonGroup}>
                            <button
                                style={{
                                    ...styles.button,
                                    backgroundColor: isWish ? '#FF3366' : '#4080ff',
                                }}
                                onClick={handleWishClick}
                            >
                                {isWish ? '찜 완료' : '찜'}
                            </button>
                        </div>
                    </div>

                    {/* 코멘트 입력란 */}
                    {showCommentInput && (
                        <div style={styles.commentSection}>
              <textarea
                  style={styles.commentInput}
                  placeholder="이 작품에 대한 생각을 자유롭게 표현해주세요"
                  value={comment}
                  onChange={handleCommentChange}
              />
                            <button style={styles.submitButton} onClick={handleSubmitComment}>
                                코멘트 남기기
                            </button>
                        </div>
                    )}

                    <div style={styles.details}>
                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>줄거리</div>
                            <div style={styles.sectionContent}>{movieData.overview}</div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>출연/제작</div>
                            <div style={styles.sectionContent}>
                                <div style={styles.crewGrid}>
                                    {crews.map((crew) => (
                                        <div key={crew.name} style={styles.crewMember}>
                                            <img
                                                src={
                                                    crew.profileImgUrl
                                                        ? 'http://image.tmdb.org/t/p/w200' +
                                                        crew.profileImgUrl
                                                        : '/default-profile-image.jpg'
                                                }
                                                alt={crew.name}
                                                style={styles.crewImage}
                                            />
                                            <div style={styles.crewInfo}>
                                                <div style={styles.crewName}>{crew.name}</div>
                                                <div style={styles.crewCharName}>{crew.charName}</div>
                                                <div style={styles.crewRole}>
                                                    {crew.role === 'CAST'
                                                        ? '출연'
                                                        : crew.role === 'DIRECTOR'
                                                            ? '감독'
                                                            : crew.role}
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
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
        padding: '210px 20px',
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
    ratingAndWish: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '20px',
    },
    myRating: {
        //marginBottom: '20px',
    },
    ratingLabel: {
        fontSize: '18px',
        fontWeight: 'bold',
        marginRight: '10px',
        color: '#000000',
    },
    stars: {
        display: 'inline-block',
        //marginLeft: '10px',
    },
    starFilled: {
        color: '#f8d90f',
        cursor: 'pointer',
    },
    starEmpty: {
        color: '#ccc',
        cursor: 'pointer',
    },
    starIcon: {
        fontSize: '40px',
    },
    buttonGroup: {
        //display: 'flex',
        //marginBottom: '20px',
    },
    button: {
        //marginRight: '10px',
        padding: '10px 20px',
        //backgroundColor: '#4080ff',
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
        color: '#000000',
    },
    sectionContent: {
        color: '#000000',
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
    crewGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))',
        gap: '10px',
    },
    crewMember: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        textAlign: 'center',
    },
    crewImage: {
        width: '100px',
        height: '100px',
        borderRadius: '50%',
        objectFit: 'cover',
        marginBottom: '5px',
    },
    crewInfo: {},
    crewName: {
        fontWeight: 'bold',
        color: '#000000',
    },
    crewCharName: {
        color: '#000000',
    },
    crewRole: {
        fontSize: '0.9em',
        color: '#000000',
    },
    commentSection: {
        marginTop: '20px',
        display: 'flex',
        flexDirection: 'column',
    },
    commentInput: {
        padding: '10px',
        border: '1px solid #ccc',
        borderRadius: '5px',
        marginBottom: '10px',
        resize: 'vertical',
        height: '100px',
    },
    submitButton: {
        padding: '10px 20px',
        backgroundColor: '#4080ff',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        alignSelf: 'flex-end',
    },
};

export default MovieDetailPage;