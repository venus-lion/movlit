import React, { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../axiosInstance';
import {
    FaComment,
    FaHeart,
    FaRegHeart,
    FaUserCircle,
    FaStar,
    FaRegStar,
    FaStarHalfAlt,
} from 'react-icons/fa';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import MovieCarousel from '../pages/MovieCarousel';
import useAuthMovieList from '../hooks/useAuthMovieList';
import useBookList from '../hooks/useBookList';
import BookGenreCarousel from '../pages/BookGenreCarousel';
import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';

function MovieDetailPage() {
    const { movieId } = useParams();
    const [movieData, setMovieData] = useState(null);
    const [myRating, setMyRating] = useState(0); // 0~10 사이의 값 (0.5 단위)
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
    const [userComment, setUserComment] = useState(null);
    const [userCommentId, setUserCommentId] = useState(null);
    const loader = useRef(null);

    const initialVisibleCrews = 14;

    // 관련 영화 데이터 가져오기
    const {
        movies: relatedMovies,
        loading: relatedMoviesLoading,
        error: relatedMoviesError,
    } = useAuthMovieList({
        endpoint: `movies/${movieId}/detail/related`,
        params: { pageSize: 30 },
    });
    const [relatedMoviesStartIndex, setRelatedMoviesStartIndex] = useState(0);

    const handleRelatedMoviesNext = () => {
        const newIndex = relatedMoviesStartIndex + 5;
        if (newIndex < relatedMovies.length) {
            setRelatedMoviesStartIndex(newIndex);
        }
    };

    const handleRelatedMoviesPrev = () => {
        const newIndex = relatedMoviesStartIndex - 5;
        if (newIndex >= 0) {
            setRelatedMoviesStartIndex(newIndex);
        }
    };

    // 관련 장르 도서 데이터 가져오기
    const {
        books: relatedBooks,
        loading: relatedBooksLoading,
        error: relatedBooksError,
    } = useBookList({
        endpoint: `/api/books/genres/movies/${movieId}/detail`,
        params: { limit: 30 },
    });
    const [relatedBooksStartIndex, setRelatedBooksStartIndex] = useState(0);

    const handleRelatedBooksNext = () => {
        const newIndex = relatedBooksStartIndex + 5;
        if (newIndex < relatedBooks.length) {
            setRelatedBooksStartIndex(newIndex);
        }
    };

    const handleRelatedBooksPrev = () => {
        const newIndex = relatedBooksStartIndex - 5;
        if (newIndex >= 0) {
            setRelatedBooksStartIndex(newIndex);
        }
    };

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
                    voteAverage: data.voteAverage,
                    tagline: data.tagline,
                    ratingCount: data.voteCount,
                    isHearted: data.isHearted,
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
            const options = {
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
                    // 각 코멘트 객체에 isLiked와 commentLikeCount, profileImgUrl이 존재하는지 확인하고, 값이 없으면 false, 0, null으로 설정
                    const updatedComments = response.data.content.map((comment) => ({
                        ...comment,
                        isLiked: comment.isLiked || false,
                        commentLikeCount: comment.commentLikeCount || 0,
                        profileImgUrl: comment.profileImgUrl || null,
                    }));
                    setComments(updatedComments);
                    setHasMore(
                        response.data.content.length > 4 || fetchedTotalComments > 4
                    );
                } else {
                    // 마찬가지로 isLiked와 commentLikeCount, profileImgUrl 존재 여부 확인 및 기본값 설정
                    const updatedComments = response.data.content.map((comment) => ({
                        ...comment,
                        isLiked: comment.isLiked || false,
                        commentLikeCount: comment.commentLikeCount || 0,
                        profileImgUrl: comment.profileImgUrl || null,
                    }));
                    setComments((prevComments) => [...prevComments, ...updatedComments]);
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

    // 코멘트 별점 변경 핸들러 수정
    const handleRatingChange = (newRating) => {
        // 사용자가 별 반 개를 클릭한 경우에도 적절히 처리
        setMyRating(newRating);
        if(newRating > 0){
            setShowCommentInput(true);
        }
    };

    // 찜하기/찜해제 처리
    const handleWishClick = async () => {
        try {
            let updatedHeartCount;

            if (movieData.isHearted) {
                // 찜 해제 (DELETE 요청)
                await axiosInstance.delete(`/movies/${movieId}/hearts`);
                updatedHeartCount = movieData.heartCount - 1;
                setMovieData((prevMovieData) => ({
                    ...prevMovieData,
                    heartCount: updatedHeartCount,
                    isHearted: false,
                }));
                toast.success('찜 목록에서 제거되었습니다.');
            } else {
                // 찜하기 (POST 요청)
                const response = await axiosInstance.post(`/movies/${movieId}/hearts`);
                updatedHeartCount = response.data.movieHeartCnt;
                setMovieData((prevMovieData) => ({
                    ...prevMovieData,
                    heartCount: updatedHeartCount,
                    isHearted: true,
                }));
                toast.success('찜 목록에 추가되었습니다.');
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
                heartCountSpan.textContent = updatedHeartCount.toLocaleString();
            }
        } catch (error) {
            console.error('Error updating wish status:', error);
            toast.error('찜하기/찜해제 처리에 실패했습니다.');
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
            toast.error('별점을 입력해주세요.');
            return;
        }
        const currentComment = userComment ? myComment : comment;
        if (currentComment.trim() === '') {
            toast.error('코멘트를 입력해주세요.');
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
                toast.success('코멘트가 수정되었습니다.');
            } else {
                // 새 코멘트 저장 (POST 요청)
                await axiosInstance.post(`/movies/${movieId}/comments`, requestBody);
                toast.success('코멘트가 저장되었습니다.');
            }

            // 코멘트 상태 업데이트
            fetchUserComment();
            fetchComments(0);
        } catch (error) {
            console.error('코멘트 저장/수정 실패:', error);
            toast.error('코멘트 저장/수정에 실패했습니다.');
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
            toast.success('코멘트가 삭제되었습니다.');
            fetchUserComment();
            fetchComments(0);
        } catch (error) {
            console.error('Error deleting comment:', error);
            toast.error('코멘트 삭제에 실패했습니다.');
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

    // 좋아요/좋아요 취소 처리
    const handleLikeClick = async (commentId, isLiked) => {
        try {
            if (isLiked) {
                // 좋아요 취소 (DELETE 요청)
                await axiosInstance.delete(`/movies/comments/${commentId}/likes`);
                toast.success('좋아요를 취소했습니다.');
            } else {
                // 좋아요 (POST 요청)
                await axiosInstance.post(`/movies/comments/${commentId}/likes`);
                toast.success('좋아요를 눌렀습니다.');
            }

            // 코멘트 목록 다시 불러오기
            fetchComments(0);
        } catch (error) {
            console.error('Error updating like status:', error);
            toast.error('좋아요/좋아요 취소 처리에 실패했습니다.');
        }
    };

    // 별을 표시하는 함수 수정 (반 별: 1점, 온전한 별: 2점으로 계산)
    const renderStars = (rating) => {
        const fullStars = Math.floor(rating / 2); // 온전한 별의 개수 (2점당 1개)
        const halfStar = rating % 2 === 1; // 반 별의 여부 (나머지가 1이면 반 별)
        const emptyStars = 5 - fullStars - (halfStar ? 1 : 0); // 빈 별의 개수

        return (
            <>
                {[...Array(fullStars)].map((_, index) => (
                    <FaStar key={`full-${index}`} style={styles.starFilled} />
                ))}
                {halfStar && <FaStarHalfAlt style={styles.starFilled} />}
                {[...Array(emptyStars)].map((_, index) => (
                    <FaRegStar key={`empty-${index}`} style={styles.starEmpty} />
                ))}
            </>
        );
    };

    if (!movieData) {
        return <div style={styles.loading}>Loading...</div>;
    }

    // 유니크한 장르 세트 생성
    const uniqueGenres = new Set();
    if (relatedBooks) {
        relatedBooks.forEach((book) => {
            if (book.genres && Array.isArray(book.genres)) {
                book.genres.forEach((genre) => uniqueGenres.add(genre.genreName));
            }
        });
    }
    const uniqueGenreList = Array.from(uniqueGenres);

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
                <ToastContainer />
                <div style={styles.breadcrumbs}>
                    홈 / 영화 / {movieData.title}
                </div>
                <div style={styles.title}>{movieData.title}</div>
                <div style={styles.subtitle}>
                    {movieData.releaseDate
                        ? movieData.releaseDate.substring(0, 4)
                        : ''}{' '}
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
                                {/* 별 5개로 10점 만점 표현 */}
                                {[...Array(5)].map((_, index) => {
                                    const starIndex = (index + 1); // 별 1개당 2점씩 계산 (1, 2, 3, 4, 5)
                                    return (
                                        <span
                                            key={index}
                                            onClick={() => handleRatingChange(starIndex * 2)} // 클릭 시 handleRatingChange에 starIndex * 2를 전달 (2, 4, 6, 8, 10)
                                            style={{ cursor: 'pointer', position: 'relative', display: 'inline-block' }}
                                            onMouseMove={(e) => {
                                                // 별 아이콘의 왼쪽 절반 클릭 시 반 별만 칠해지도록
                                                const rect = e.currentTarget.getBoundingClientRect();
                                                const x = e.clientX - rect.left;
                                                const halfWidth = rect.width / 2;

                                                if (x <= halfWidth) {
                                                    setMyRating(starIndex * 2 - 1);
                                                } else {
                                                    setMyRating(starIndex * 2);
                                                }
                                            }}
                                            onMouseLeave={() => {
                                                // onMouseLeave 시 myRating을 최신 DB값으로
                                                fetchUserComment();
                                            }}
                                        >
                                            {/* 별 1개당 2점씩 계산하여 꽉 찬 별, 반 별, 빈 별 표시 */}
                                            {starIndex * 2 <= myRating ? (
                                                <FaStar style={styles.starFilled} />
                                            ) : starIndex * 2 === myRating + 1 ? (
                                                <FaStarHalfAlt style={styles.starFilled} />
                                            ) : (
                                                <FaRegStar style={styles.starEmpty} />
                                            )}
                                        </span>
                                    );
                                })}
                            </div>
                        </div>
                        <div style={styles.averageRating}>
                            <span style={styles.ratingLabel}>평균 별점</span>
                            <div style={styles.starsAndProgress}>
                                <div style={styles.stars}>
                                    {renderStars(movieData.voteAverage)}
                                </div>
                                {/* Circular Progress Bar 추가 */}
                                <div style={styles.progressBarContainer}>
                                    <CircularProgressbar
                                        value={movieData.voteAverage * 10}
                                        maxValue={100}
                                        text={`${Math.round(movieData.voteAverage * 10) / 10}`}
                                        styles={buildStyles({
                                            textSize: '22px',
                                            pathColor: '#f8d90f',
                                            textColor: '#000000',
                                            trailColor: '#d6d6d6',
                                        })}
                                    />
                                </div>
                            </div>
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
                            <span id="heartCount" style={styles.heartCountContainer}>
                {movieData.heartCount.toLocaleString()}
              </span>
                        </div>
                    </div>

                    {/* 사용자 코멘트 표시 */}
                    {userComment && userComment.score > 0 && (
                        <div style={styles.userCommentDisplay}>
                            <div style={styles.userInfo}>
                                {userComment.profileImgUrl ? (
                                    <img
                                        src={userComment.profileImgUrl}
                                        alt="프로필 이미지"
                                        style={styles.profileImage}
                                    />
                                ) : (
                                    <FaUserCircle style={styles.defaultProfileIcon}/>
                                )}
                                <span style={styles.userNickname}>{userComment.nickname}</span>
                            </div>
                            <div style={styles.userCommentContent}>
                                <FaComment style={styles.commentIcon} />
                                <p style={styles.userCommentText}>{userComment.comment}</p>
                            </div>
                        </div>
                    )}

                    {/* 코멘트 입력 및 수정/삭제 버튼 */}
                    {myRating > 0 && showCommentInput && (
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
                            <button style={styles.deleteButton} onClick={handleDeleteComment}>
                                삭제하기
                            </button>
                        </div>
                    )}

                    <div style={{ marginTop: '20px' }} />

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
                                                        ? 'http://image.tmdb.org/t/p/w200' +
                                                        crew.profileImgUrl
                                                        : '/default-profile-image.jpg'
                                                }
                                                alt={crew.name}
                                                style={styles.crewImage}
                                            />
                                            <div style={styles.crewInfo}>
                                                <div style={styles.crewName}>{crew.name}</div>
                                                <div style={styles.crewCharName}>
                                                    {crew.charName}
                                                </div>
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
                                            <div style={styles.commentUserInfo}>
                                                {/* 프로필 이미지/아이콘 표시 */}
                                                {comment.profileImgUrl ? (
                                                    <img
                                                        src={comment.profileImgUrl}
                                                        alt="프로필 이미지"
                                                        style={styles.commentProfileImage}
                                                    />
                                                ) : (
                                                    <FaUserCircle style={styles.defaultProfileIcon}/>
                                                )}
                                                <span style={styles.commentUser}>{comment.nickname}</span>
                                            </div>
                                            {/* 별점 및 좋아요 컨테이너 */}
                                            <div style={styles.commentActions}>
                                                {/* 코멘트 별점 표시 */}
                                                <div style={styles.commentRating}>
                                                    {/* 별 5개로 10점 만점 표현 */}
                                                    {[...Array(5)].map((_, index) => {
                                                        const starIndex = (index + 1);
                                                        return (
                                                            <span key={index} style={{display: 'inline-block'}}>
                                                                {starIndex * 2 <= comment.score ? (
                                                                    <FaStar style={styles.commentStarFilled}/>
                                                                ) : starIndex * 2 === comment.score + 1 ? (
                                                                    <FaStarHalfAlt style={styles.commentStarFilled}/>
                                                                ) : (
                                                                    <FaRegStar style={styles.commentStarEmpty}/>
                                                                )}
                                                            </span>
                                                        );
                                                    })}
                                                    {/*<span style={styles.commentScore}>{comment.score}</span>*/}
                                                </div>
                                                {/* 좋아요 버튼 및 카운트 컨테이너 */}
                                                <div style={styles.likeContainer}>
                                                    <button
                                                        style={styles.likeButton}
                                                        onClick={() => handleLikeClick(comment.movieCommentId, comment.isLiked)}
                                                    >
                                                        {comment.isLiked ? <FaHeart style={styles.likedIcon}/> :
                                                            <FaRegHeart style={styles.likeIcon}/>}
                                                    </button>
                                                    {/* 좋아요 카운트 */}
                                                    <span
                                                        style={styles.likeCountContainer}>{comment.commentLikeCount}</span>
                                                </div>
                                            </div>
                                        </div>
                                        {/* 코멘트 내용 */}
                                        <div style={styles.commentContent}>
                                            <FaComment style={styles.commentIcon}/>
                                            <p style={styles.commentText}>{comment.comment}</p>
                                        </div>
                                    </div>
                                ))}
                                {/* 무한 스크롤 로딩 감지 Element */}
                                <div ref={loader}/>
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
                                        <button style={styles.moreButton} onClick={handleShowLessComments}>
                                            더보기 취소
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* 새로운 관련 영화 섹션 */}
                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>관련 영화 추천</div>
                            <div style={styles.sectionContent}>
                                {relatedMoviesLoading && <p>Loading related movies...</p>}
                                {relatedMoviesError && (
                                    <div>
                                        <p>Error loading related movies.</p>
                                    </div>
                                )}
                                {!relatedMoviesLoading && !relatedMoviesError && (
                                    <MovieCarousel
                                        movies={relatedMovies}
                                        startIndex={relatedMoviesStartIndex}
                                        handleNext={handleRelatedMoviesNext}
                                        handlePrev={handleRelatedMoviesPrev}
                                    />
                                )}
                            </div>
                        </div>

                        {/* 관련 도서 섹션 */}
                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>관련 도서 추천</div>
                            <div style={styles.sectionContent}>
                                {relatedBooksLoading && <p>Loading related books...</p>}
                                {relatedBooksError && (
                                    <div>
                                        <p>Error loading related books.</p>
                                    </div>
                                )}
                                {!relatedBooksLoading && !relatedBooksError && (
                                    <BookGenreCarousel
                                        books={relatedBooks}
                                        startIndex={relatedBooksStartIndex}
                                        handleNext={handleRelatedBooksNext}
                                        handlePrev={handleRelatedBooksPrev}
                                    />
                                )}
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
        alignItems: 'center', // 세로 중앙 정렬
        //marginLeft: '10px',
    },
    starFilled: {
        color: '#f8d90f',
        cursor: 'pointer',
        fontSize: '40px', // 크기 조정
    },
    starEmpty: {
        color: '#ccc',
        cursor: 'pointer',
        fontSize: '40px', // 크기 조정
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
        justifyContent: 'space-between'
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
        marginTop: '20px',
        padding: '15px',
        border: '1px solid #ccc',
        borderRadius: '5px',
        backgroundColor: '#f8f8f8',
    },
    commentActions: {
        marginTop: '10px',
        display: 'flex',
        gap: '10px',
        justifyContent: 'flex-end',
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
        marginBottom: '10px',
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
        backgroundColor: '#f2f2f2',
        borderRadius: '10px',
        border: '1px solid #ccc',
        fontSize: '14px',
        fontWeight: 'bold',
        color: '#333',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
    },
    likeButton: {
        marginLeft: '10px',
        padding: '0px',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '14px',
        background: 'none',
    },
    likeIcon: {
        color: '#4080ff',
        fontSize: '1.2em',
    },
    likedIcon: {
        color: '#FF3366',
        fontSize: '1.2em',
    },
    likeContainer: {
        display: 'flex',
        alignItems: 'center',
    },
    likeCountContainer: {
        marginLeft: '5px',
        padding: '3px 6px',
        backgroundColor: '#f2f2f2',
        borderRadius: '8px',
        border: '1px solid #ccc',
        fontSize: '12px',
        fontWeight: 'bold',
        color: '#333',
    },
    defaultProfileIcon: {
        fontSize: '30px',
        color: '#999',
        marginRight: '10px',
    },
    userCommentContent: {
        display: 'flex',
    },
    commentIcon: {
        fontSize: '18px',
        color: '#666',
        marginRight: '5px',
        marginTop: '3px'
    },
    userCommentText: {
        fontSize: '14px',
        lineHeight: '1.4',
    },
    commentUserInfo: {
        display: 'flex',
        alignItems: 'center',
    },
    commentProfileImage: {
        width: '30px',
        height: '30px',
        borderRadius: '50%',
        marginRight: '5px',
    },
    commentRating: {
        display: 'flex',
        alignItems: 'center',
        marginLeft: 'auto',
    },
    commentScore: {
        marginLeft: '5px',
        color: '#000000',
    },
    commentContent: {
        display: 'flex',
        alignItems: 'flex-start',
        marginLeft: '5px',
    },
    averageRating: {
        marginLeft: '20px',
        display: 'flex', // 가로 정렬
        alignItems: 'center', // 세로 중앙 정렬
    },
    voteAverage: {
        marginLeft: '10px',
        fontSize: '16px',
        color: '#000000',
    },
    starsAndProgress: {
        display: 'flex',
        alignItems: 'center',
    },
    progressBarContainer: {
        width: '60px', // 원하는 크기로 조정
        marginLeft: '15px', // 별점과의 간격 조정
    }
};

export default MovieDetailPage;