import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../axiosInstance';

function MovieDetailPage() {
    const { movieId } = useParams();
    const [movieData, setMovieData] = useState(null);
    const [myRating, setMyRating] = useState(0);
    const [myComment, setMyComment] = useState('');
    const [crews, setCrews] = useState([]);
    const [visibleCrews, setVisibleCrews] = useState([]);
    const [showMoreCrews, setShowMoreCrews] = useState(false);
    const [genres, setGenres] = useState([]);
    const [showCommentInput, setShowCommentInput] = useState(false);
    const [comment, setComment] = useState('');
    const [comments, setComments] = useState([]);
    const [hasMore, setHasMore] = useState(true);
    const [showLessComments, setShowLessComments] = useState(false);
    const [totalComments, setTotalComments] = useState(0);
    const [page, setPage] = useState(0);
    const [isInitialLoad, setIsInitialLoad] = useState(true);
    const [userComment, setUserComment] = useState(null); // 사용자 코멘트 정보
    const [userCommentId, setUserCommentId] = useState(null); // 사용자 코멘트 ID
    const loader = useRef(null);

    const initialVisibleCrews = 14;

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
                    posterUrl: data.posterPath.replace('original', 'w200'),
                    backdropUrl: data.backdropPath,
                    releaseDate: data.releaseDate,
                    country: data.productionCountry,
                    language: data.originalLanguage,
                    runtime: data.runtime,
                    status: data.status,
                    voteCount: data.voteCount,
                    tagline: data.tagline,
                    ratingCount: data.voteCount,
                    isHearted: data.isHearted, // isHearted 추가
                });
            })
            .catch((error) => console.error('Error fetching movie data:', error));

        axiosInstance
            .get(`/movies/${movieId}/crews`)
            .then((response) => {
                const sortedCrews = response.data.sort((a, b) => {
                    if (a.role === 'DIRECTOR' && b.role !== 'DIRECTOR') {
                        return -1;
                    } else if (a.role !== 'DIRECTOR' && b.role === 'DIRECTOR') {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                setCrews(sortedCrews);
                setVisibleCrews(sortedCrews.slice(0, initialVisibleCrews));
            })
            .catch((error) => console.error('Error fetching crew data:', error));

        axiosInstance
            .get(`/movies/${movieId}/genres`)
            .then((response) => {
                const formattedGenres = response.data.map((genre) => ({
                    name: genre.genreName,
                }));
                setGenres(formattedGenres);
            })
            .catch((error) => console.error('Error fetching genre data:', error));

        fetchUserComment();
        fetchComments(0);
    }, [movieId]);

    // Intersection Observer 설정 (코멘트 무한 스크롤)
    useEffect(() => {
        if (!isInitialLoad) {
            var options = {
                root: null,
                rootMargin: '20px',
                threshold: 1.0,
            };

            const observer = new IntersectionObserver(handleObserver, options);
            if (loader.current) {
                observer.observe(loader.current);
            }

            return () => observer.disconnect();
        }
    }, [comments, hasMore, isInitialLoad]);

    // 사용자 코멘트 가져오기
    const fetchUserComment = async () => {
        try {
            const response = await axiosInstance.get(`/movies/${movieId}/myComment`);
            if (response.data) {
                const { movieCommentId, comment, score, nickname, profileImgUrl } =
                    response.data;
                setUserComment({
                    nickname,
                    profileImgUrl,
                    comment,
                    score,
                });
                setUserCommentId(movieCommentId);
                setMyRating(score);
                setMyComment(comment);
                if (score > 0) {
                    setShowCommentInput(false);
                } else {
                    setShowCommentInput(true);
                }
            } else {
                setUserComment(null);
                setUserCommentId(null);
                setMyRating(0);
                setMyComment('');
                setShowCommentInput(false);
            }
        } catch (error) {
            console.error('Error fetching user comment:', error);
            setUserComment(null);
            setUserCommentId(null);
        }
    };

    // 코멘트 불러오기 함수
    const fetchComments = (currentPage = 0) => {
        axiosInstance
            .get(`/movies/${movieId}/comments?page=${currentPage}`)
            .then((response) => {
                const fetchedTotalComments =
                    response.data.content && response.data.content.length > 0
                        ? response.data.content[0].commentCount
                        : 0;
                setTotalComments(fetchedTotalComments);

                if (currentPage === 0) {
                    setComments(response.data.content.slice(0, 4));
                    setHasMore(
                        response.data.content.length > 4 || fetchedTotalComments > 4
                    );
                } else {
                    setComments((prevComments) => [
                        ...prevComments,
                        ...response.data.content,
                    ]);
                    setHasMore(!response.data.last);
                }
                setPage(currentPage + 1);
            })
            .catch((error) => console.error('Error fetching comments:', error));
    };

    // Intersection Observer 콜백 함수
    const handleObserver = (entities) => {
        const target = entities[0];
        if (target.isIntersecting && hasMore && !isInitialLoad) {
            fetchComments(page);
        }
    };

    const handleRatingChange = (newRating) => {
        if (userComment) {
            setMyRating(newRating);
            setShowCommentInput(true);
        } else {
            if (myRating === newRating) {
                setMyRating(0);
                setShowCommentInput(false);
            } else {
                setMyRating(newRating);
                setShowCommentInput(true);
            }
        }
    };

    // 찜하기/찜해제 처리
    const handleWishClick = async () => {
        try {
            if (movieData.isHearted) {
                // 찜 해제 (DELETE 요청)
                await axiosInstance.delete(`/movies/${movieId}/hearts`);
                setMovieData((prevMovieData) => ({
                    ...prevMovieData,
                    heartCount: prevMovieData.heartCount - 1,
                    isHearted: false,
                }));
            } else {
                // 찜하기 (POST 요청)
                const response = await axiosInstance.post(`/movies/${movieId}/hearts`);
                setMovieData((prevMovieData) => ({
                    ...prevMovieData,
                    heartCount: response.data.movieHeartCnt,
                    isHearted: true,
                }));
            }

            // 찜 상태에 따라 버튼 및 카운트 업데이트
            const button = document.getElementById('wishButton');
            const heartCountSpan = document.getElementById('heartCount');

            if (button) {
                button.style.backgroundColor = !movieData.isHearted
                    ? '#FF3366'
                    : '#4080ff';
            }

            if (heartCountSpan) {
                if (!movieData.isHearted) {
                    heartCountSpan.style.display = 'none';
                } else {
                    heartCountSpan.style.display = 'inline-block';
                }
            }
        } catch (error) {
            console.error('Error updating wish status:', error);
            alert('찜하기/찜해제 처리에 실패했습니다.');
        }
    };

    const handleCommentChange = (event) => {
        if (userComment) {
            setMyComment(event.target.value);
        } else {
            setComment(event.target.value);
        }
    };

    // 코멘트 제출
    const handleSubmitComment = async () => {
        if (myRating === 0) {
            alert('별점을 입력해주세요.');
            return;
        }
        const currentComment = userComment ? myComment : comment;
        if (currentComment.trim() === '') {
            alert('코멘트를 입력해주세요.');
            return;
        }

        const requestBody = {
            score: myRating,
            comment: currentComment,
        };

        try {
            if (userCommentId) {
                // 코멘트 수정 (PUT 요청)
                await axiosInstance.put(
                    `/movies/comments/${userCommentId}`,
                    requestBody
                );
                alert('코멘트가 수정되었습니다.');
            } else {
                // 새 코멘트 저장 (POST 요청)
                await axiosInstance.post(`/movies/${movieId}/comments`, requestBody);
                alert('코멘트가 저장되었습니다.');
            }

            // 코멘트 상태 업데이트
            fetchUserComment();
            fetchComments(0);
        } catch (error) {
            console.error('코멘트 저장/수정 실패:', error);
            alert('코멘트 저장/수정에 실패했습니다.');
        } finally {
            setComment('');
            setMyRating(0); // 코멘트 제출 후 별점을 다시 0으로 설정
            setShowCommentInput(false);
        }
    };

    // 코멘트 삭제
    const handleDeleteComment = async () => {
        if (!userCommentId) return;

        try {
            // 코멘트 삭제 (DELETE 요청)
            await axiosInstance.delete(`/movies/comments/${userCommentId}`);
            alert('코멘트가 삭제되었습니다.');
            fetchUserComment();
            fetchComments(0);
        } catch (error) {
            console.error('Error deleting comment:', error);
            alert('코멘트 삭제에 실패했습니다.');
        }
    };

    // 출연/제작진 더보기/취소 처리
    const handleShowMoreCrews = () => {
        setShowMoreCrews(true);
        setVisibleCrews(crews);
    };

    const handleShowLessCrews = () => {
        setShowMoreCrews(false);
        setVisibleCrews(crews.slice(0, initialVisibleCrews));
    };

    // 코멘트 더보기 처리
    const handleLoadMore = () => {
        setIsInitialLoad(false);
        fetchComments(page);
        setShowLessComments(true);
    };

    // 코멘트 더보기 취소 처리
    const handleShowLessComments = () => {
        setComments(comments.slice(0, 4));
        setShowLessComments(false);
        setHasMore(true);
        setPage(1);
        setIsInitialLoad(true);
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
                <div style={styles.breadcrumbs}>
                    홈 / 영화 / {movieData.title}
                </div>
                <div style={styles.title}>{movieData.title}</div>
                <div style={styles.subtitle}>
                    {movieData.releaseDate ? movieData.releaseDate.substring(0, 4) : ''}{' '}
                    ・{' '}
                    {genres.map((genre, index) => (
                        <span key={index}>
              {genre.name}
                            {index < genres.length - 1 ? ', ' : ''}
            </span>
                    ))}{' '}
                    ・ {movieData.country}
                </div>
            </div>

            <div style={styles.mainContent}>
                <div style={styles.poster}>
                    <img src={movieData.posterUrl} alt={movieData.title} />
                </div>

                <div style={styles.info}>
                    <div style={styles.ratingAndWish}>
                        <div style={styles.myRating}>
                            <span style={styles.ratingLabel}>내 별점</span>
                            <div style={styles.stars}>
                                {Array(5)
                                    .fill()
                                    .map((_, index) => (
                                        <span
                                            key={index}
                                            style={
                                                index < myRating
                                                    ? styles.starFilled
                                                    : styles.starEmpty
                                            }
                                            onClick={() => handleRatingChange(index + 1)}
                                        >
                      <span style={styles.starIcon}>★</span>
                    </span>
                                    ))}
                            </div>
                            {/* 사용자 코멘트, 별점, 닉네임, 프로필 이미지 표시 */}
                            {userComment && userComment.score > 0 && (
                                <div style={styles.userCommentDisplay}>
                                    <div style={styles.userInfo}>
                                        <img
                                            src={
                                                userComment.profileImgUrl || '/default-profile-image.jpg'
                                            }
                                            alt="프로필 이미지"
                                            style={styles.profileImage}
                                        />
                                        <span style={styles.userNickname}>
                      {userComment.nickname}
                    </span>
                                    </div>
                                    <p>
                    <span
                        style={{ ...styles.commentStarFilled, marginRight: '5px' }}
                    >
                      ★
                    </span>
                                        {userComment.score}
                                    </p>
                                    <p>{userComment.comment}</p>
                                </div>
                            )}
                        </div>
                        <div style={styles.buttonGroup}>
                            <button
                                id="wishButton"
                                style={{
                                    ...styles.button,
                                    backgroundColor: movieData.isHearted
                                        ? '#FF3366'
                                        : '#4080ff',
                                }}
                                onClick={handleWishClick}
                            >
                                {movieData.isHearted ? '찜 완료' : '찜'}
                            </button>
                            {movieData.isHearted && (
                                <span id="heartCount" style={styles.heartCountContainer}>
                  {movieData.heartCount.toLocaleString()}
                </span>
                            )}
                        </div>
                    </div>

                    {/* 코멘트 입력 및 수정/삭제 버튼 */}
                    {showCommentInput && (
                        <div style={styles.commentSection}>
              <textarea
                  style={styles.commentInput}
                  placeholder="이 작품에 대한 생각을 자유롭게 표현해주세요"
                  value={userComment ? myComment : comment}
                  onChange={handleCommentChange}
              />
                            <button style={styles.submitButton} onClick={handleSubmitComment}>
                                {userComment ? '수정하기' : '코멘트 남기기'}
                            </button>
                        </div>
                    )}

                    {/* 코멘트 삭제 및 수정 버튼 */}
                    {!showCommentInput && userComment && (
                        <div style={styles.commentActions}>
                            <button style={styles.deleteButton} onClick={handleDeleteComment}>
                                삭제하기
                            </button>
                            <button
                                style={styles.editButton}
                                onClick={() => {
                                    setMyRating(userComment.score);
                                    setMyComment(userComment.comment);
                                    setShowCommentInput(true);
                                }}
                            >
                                수정하기
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
                                    {visibleCrews.map((crew) => (
                                        <div key={crew.name} style={styles.crewMember}>
                                            <img
                                                src={
                                                    crew.profileImgUrl
                                                        ? 'http://image.tmdb.org/t/p/w200' + crew.profileImgUrl
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
                                {!showMoreCrews && crews.length > initialVisibleCrews && (
                                    <div style={styles.moreButtonContainer}>
                                        <button
                                            style={styles.moreButton}
                                            onClick={handleShowMoreCrews}
                                        >
                                            더보기
                                        </button>
                                    </div>
                                )}
                                {showMoreCrews && (
                                    <div style={styles.moreButtonContainer}>
                                        <button
                                            style={styles.moreButton}
                                            onClick={handleShowLessCrews}
                                        >
                                            더보기 취소
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>
                                코멘트{' '}
                                <span style={styles.commentCount}>
                  {totalComments.toLocaleString()}
                </span>
                            </div>
                            <div style={styles.sectionContent}>
                                {comments.map((comment) => (
                                    <div key={comment.movieCommentId} style={styles.commentItem}>
                                        <div style={styles.commentHeader}>
                      <span style={styles.commentUser}>
                        {comment.nickname}
                          <span
                              style={
                                  comment.score >= 1
                                      ? styles.commentStarFilled
                                      : styles.commentStarEmpty
                              }
                          >
                          ★
                        </span>
                          {comment.score}
                      </span>
                                        </div>
                                        <div style={styles.commentText}>{comment.comment}</div>
                                    </div>
                                ))}
                                {/* 무한 스크롤 로딩 감지 Element */}
                                <div ref={loader} />
                                {/* 더보기 버튼 */}
                                {hasMore && isInitialLoad && (
                                    <div style={styles.moreButtonContainer}>
                                        <button style={styles.moreButton} onClick={handleLoadMore}>
                                            더보기
                                        </button>
                                    </div>
                                )}
                                {/* 더보기 취소 버튼 */}
                                {showLessComments && (
                                    <div style={styles.moreButtonContainer}>
                                        <button
                                            style={styles.moreButton}
                                            onClick={handleShowLessComments}
                                        >
                                            더보기 취소
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>관련 도서</div>
                            <div style={styles.sectionContent}>
                                {movieData.relatedBooks &&
                                    movieData.relatedBooks.map((book) => (
                                        <div key={book.id} style={styles.book}>
                                            <img src={book.coverUrl} alt={book.title} />
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
        display: 'flex',
        alignItems: 'center',
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
    commentCount: {
        fontSize: '16px',
        color: '#656565',
    },
    commentItem: {
        borderBottom: '1px solid #ccc',
        padding: '10px 0',
    },
    commentHeader: {
        display: 'flex',
        alignItems: 'center',
        marginBottom: '5px',
    },
    commentUser: {
        fontWeight: 'bold',
        marginRight: '5px',
        color: '#000000',
    },
    commentStarFilled: {
        color: '#f8d90f',
        marginLeft: '5px',
    },
    commentStarEmpty: {
        color: '#ccc',
        marginLeft: '5px',
    },
    commentText: {
        color: '#000000',
    },
    moreButtonContainer: {
        display: 'flex',
        justifyContent: 'center',
        marginTop: '10px',
    },
    moreButton: {
        padding: '5px 10px',
        backgroundColor: '#4080ff',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    userCommentDisplay: {
        marginTop: '10px',
        padding: '10px',
        border: '1px solid #ccc',
        borderRadius: '5px',
    },
    commentActions: {
        marginTop: '10px',
        display: 'flex',
        gap: '10px',
    },
    deleteButton: {
        padding: '10px 20px',
        backgroundColor: '#dc3545',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    editButton: {
        padding: '10px 20px',
        backgroundColor: '#ffc107',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    userInfo: {
        display: 'flex',
        alignItems: 'center',
        marginBottom: '5px',
    },
    profileImage: {
        width: '30px',
        height: '30px',
        borderRadius: '50%',
        marginRight: '5px',
    },
    userNickname: {
        fontWeight: 'bold',
    },
    heartCountContainer: {
        marginLeft: '8px',
        padding: '4px 8px',
        backgroundColor: '#f2f2f2', // 배경색
        borderRadius: '10px', // 둥근 모서리
        border: '1px solid #ccc', // 테두리
        fontSize: '14px',
        fontWeight: 'bold',
        color: '#333', // 텍스트 색상
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)', // 그림자 효과
    },
};

export default MovieDetailPage;