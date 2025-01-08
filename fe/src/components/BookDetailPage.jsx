import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../axiosInstance';
import { FaHeart, FaRegHeart, FaUserCircle, FaComment } from 'react-icons/fa';

function BookDetailPage() {
    const {bookId} = useParams();
    const [bookData, setBookData] = useState(null);

    const [myRating, setMyRating] = useState(0);
    const [myComment, setMyComment] = useState('');
    const [crews, setCrews] = useState([]);
    const [member, setMember] = useState(null);
    const [isWish, setIsWish] = useState(false);
    const [genres, setGenres] = useState([]);
    const [showCommentInput, setShowCommentInput] = useState(false);
    const [comment, setComment] = useState('');
    const [comments, setComments] = useState([]);
    // const [visibleCrews, setVisibleCrews] = useState([]);
    // const [showMoreCrews, setShowMoreCrews] = useState(false);
    // const [genres, setGenres] = useState([]);
    // const [showCommentInput, setShowCommentInput] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const [showLessComments, setShowLessComments] = useState(false);
    const [totalComments, setTotalComments] = useState(0);
    const [page, setPage] = useState(0);
    const [isInitialLoad, setIsInitialLoad] = useState(true);
    const [userComment, setUserComment] = useState(null);
    const [bookCommentId, setBookCommentId] = useState(null);
    const loader = useRef(null);


    useEffect(() => {
        axiosInstance
            .get(`/books/${bookId}/detail`)
            .then((response) => {
                const data = response.data;
                setBookData({
                    booId: data.book_id,
                    isbn: data.isbn,
                    title: data.title,
                    publisher: data.publisher,
                    pubDate: data.pub_date,
                    overview: data.description,
                    bookImgUrl: data.book_img_url,
                    categoryName: data.category_name,
                    stockStatus: data.stock_status,
                    mallUrl: data.mall_url,
                    heartCount: data.heart_count
                });
                setCrews(data.book_crew)
            })
            .catch((error) => console.error('Error fetching book data:', error));



        fetchUserComment();
        fetchComments(0);
    }, [bookId]);

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
            const response = await axiosInstance.get(`/books/${bookId}/myComment`);
            if (response.data) {
                const { bookCommentId, comment, score, nickname, profileImgUrl, regDt, updDt } =
                    response.data;
                setUserComment({
                    nickname,
                    profileImgUrl,
                    comment,
                    score,
                    regDt,
                    updDt
                });
                setBookCommentId(bookCommentId);
                setMyRating(score);
                setMyComment(comment);
                if (score > 0) {
                    setShowCommentInput(false);
                } else {
                    setShowCommentInput(true);
                }
            } else {
                console.log("## 내 코멘트 없다~~");
                setUserComment(null);
                setBookCommentId(null);
                setMyRating(0);
                setMyComment('');
                setShowCommentInput(false);
            }
        } catch (error) {
            console.error('Error fetching user comment:', error);
            setUserComment(null);
            setBookCommentId(null);
        }
    };

    // 코멘트 불러오기 함수
    const fetchComments = (currentPage = 0) => {
        axiosInstance
            .get(`/books/${bookId}/comments?page=${currentPage}`)
            .then((response) => {
                const fetchedTotalComments =
                    response.data.content && response.data.content.length > 0
                        ? response.data.content[0].allCommentsCount
                        : 0;
                setTotalComments(fetchedTotalComments);

                if (currentPage === 0) {
                    // 각 코멘트 객체에 isLiked와 commentLikeCount, profileImgUrl이 존재하는지 확인하고, 값이 없으면 false, 0, null으로 설정
                    const updatedComments = response.data.content.map(comment => ({
                        ...comment,
                        isLiked: comment.liked || false,
                        commentLikeCount: comment.likeCount || 0,
                        profileImgUrl: comment.profileImgUrl || null,
                    }));
                    setComments(updatedComments);
                    setHasMore(
                        response.data.content.length > 4 || fetchedTotalComments > 4
                    );
                } else {
                    // 마찬가지로 isLiked와 commentLikeCount, profileImgUrl 존재 여부 확인 및 기본값 설정
                    const updatedComments = response.data.content.map(comment => ({
                        ...comment,
                        isLiked: comment.liked || false,
                        commentLikeCount: comment.likeCount || 0,
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
            let updatedHeartCount;

            if (bookData.isHearted) {
                // 찜 해제 (DELETE 요청)
                await axiosInstance.delete(`/books/${bookId}/hearts`);
                updatedHeartCount = bookData.heartCount - 1;
                setBookData((prevbookData) => ({
                    ...prevbookData,
                    heartCount: updatedHeartCount,
                    isHearted: false,
                }));
            } else {
                // 찜하기 (POST 요청)
                const response = await axiosInstance.post(`/books/${bookId}/hearts`);
                updatedHeartCount = bookData.heartCount + 1;
                setBookData((prevbookData) => ({
                    ...prevbookData,
                    heartCount: updatedHeartCount,
                    isHearted: true,
                }));
            }

            // 찜 상태에 따라 버튼 및 카운트 업데이트
            const button = document.getElementById('wishButton');
            const heartCountSpan = document.getElementById('heartCount');

            if (button) {
                button.style.backgroundColor = !bookData.isHearted
                    ? '#FF3366'
                    : '#4080ff';
            }

            if (heartCountSpan) {
                heartCountSpan.textContent = updatedHeartCount.toLocaleString();
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
            if (bookCommentId) {
                // 코멘트 수정 (POST 요청)
                await axiosInstance.post(
                    `/books/${bookId}/comments/${bookCommentId}`,
                    requestBody
                );
                alert('코멘트가 수정되었습니다.');
            } else {
                // 새 코멘트 저장 (POST 요청)
                await axiosInstance.post(`/books/${bookId}/comments`, requestBody);
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
        if (!bookCommentId) return;

        try {
            // 코멘트 삭제 (DELETE 요청)
            await axiosInstance.delete(`/books/${bookId}/comments/${bookCommentId}/delete`);
            alert('코멘트가 삭제되었습니다.');
            fetchUserComment();
            fetchComments(0);
        } catch (error) {
            console.error('Error deleting comment:', error);
            alert('코멘트 삭제에 실패했습니다.');
        }
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
    const handleLikeClick = async (comment, commentId, isLiked) => {
        try {
            console.log("코멘트 내용 : " + JSON.stringify(comment, null, 2))
            console.log("is 좋아요 상태 : " + isLiked);
            console.log("like 좋아요 상태2 " + comment.liked)
            if (comment.liked) {
                // 좋아요 취소 (DELETE 요청)
                await axiosInstance.delete(`/books/comments/${commentId}/likes`);
            } else {
                // 좋아요 (POST 요청)
                await axiosInstance.post(`/books/comments/${commentId}/likes`);
            }

            // 코멘트 목록 다시 불러오기
            fetchComments(0);
        } catch (error) {
            console.error('Error updating like status:', error);
            alert('좋아요/좋아요 취소 처리에 실패했습니다.');
        }
    };

    if (!bookData) {
        return <div style={styles.loading}>Loading...</div>;
    }

    return (
        <div style={styles.container}>
            <div
                style={{
                    ...styles.header,
                    backgroundColor: 'gray',
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    color: 'white',
                }}
            >
                <div style={styles.breadcrumbs}>홈 / 도서 / {bookData.title}</div>
                <div style={styles.title}>{bookData.title}</div>
                <div style={styles.subtitle}>
                    {bookData.pubDate ? bookData.pubDate.substring(0, 10).replaceAll('-', ' ・ ') : ''}
                    <br /><br />
                    {bookData.categoryName}
                    {/* 장르 목록 출력 */}
                    {/*{genres.map((genre, index) => (*/}
                    {/*    <span key={index}>{genre.name}*/}
                    {/*        /!* 마지막 장르 뒤에는 쉼표를 붙이지 않음 *!/*/}
                    {/*        {index < genres.length - 1 ? ', ' : ''}*/}
                    {/*    </span>*/}
                    {/*))}*/}
                </div>
            </div>

            <div style={styles.mainContent}>
                <div style={styles.poster}>
                    <img src={bookData.bookImgUrl} alt={bookData.title} style={styles.image} />
                    <br />
                    <div>
                        <br/>
                        <p style={{ lineHeight: '1.8', margin: '0'}}>재고 상태 </p>
                        <p style={{ lineHeight: '1.8', margin: '0'}}><strong>{bookData.stockStatus}</strong></p>
                        <br/>
                        <a href={bookData.mallUrl} target="_blank" rel="noopener noreferrer" >
                            <button>구매하기</button>
                        </a>

                    </div>
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
                        </div>
                        <div style={styles.buttonGroup}>
                            <button
                                id="wishButton"
                                style={{
                                    ...styles.button,
                                    backgroundColor: bookData.isHearted
                                        ? '#FF3366'
                                        : '#4080ff',
                                }}
                                onClick={handleWishClick}
                            >
                                {bookData.isHearted ? '찜 완료' : '찜'}
                            </button>
                            <span id="heartCount" style={styles.heartCountContainer}>
                {bookData.heartCount}
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
                                    <FaUserCircle style={styles.defaultProfileIcon} />
                                )}
                                <span style={styles.userNickname}>{userComment.nickname}</span>
                            </div>
                            <div style={styles.userCommentContent}>
                                <FaComment style={styles.commentIcon} />
                                <p style={styles.userCommentText}>{userComment.comment}</p>
                            </div>
                            <div>
                                <p style={styles.userNickname}>등록일 : {userComment.updDt.substring(0, 10)}</p>
                            </div>
                        </div>
                    )}

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

                    <div style={{marginTop: '20px'}}/>

                    <div style={styles.details}>
                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>줄거리</div>
                            <div style={styles.sectionContent}>{bookData.overview}</div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>출연/제작</div>
                            <div style={styles.sectionContent}>
                                <div style={styles.crewGrid}>
                                    {crews.map((crew) => (
                                        <div key={crew.name} style={styles.crewMember}>
                                            {/*<img*/}
                                            {/*    src={*/}
                                            {/*        crew.profileImageUrl*/}
                                            {/*    }*/}
                                            {/*    alt={crew.name}*/}
                                            {/*    style={styles.crewImage}*/}
                                            {/*/>*/}
                                            <div style={styles.crewInfo}>
                                                <div style={styles.crewName}>{crew.name}</div>
                                                <div style={styles.crewCharName}>{crew.charName}</div>
                                                <div style={styles.crewRole}>
                                                    {crew.role === 'AUTHOR'
                                                        ? '지은이'
                                                        : crew.role === 'TRANSLATOR'
                                                            ? '옮긴이'
                                                            : crew.role}
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>


                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>
                                코멘트 <span style={styles.commentCount}>{totalComments}</span>
                            </div>
                            <div style={styles.sectionContent}>
                                {comments.map((comment) => (
                                    <div key={comment.bookCommentId} style={styles.commentItem}>
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
                                                    <FaUserCircle style={styles.defaultProfileIcon} />
                                                )}
                                                <span style={styles.commentUser}>{comment.nickname}</span>
                                            </div>
                                            {/* 별점 및 좋아요 컨테이너 */}
                                            <div style={styles.commentActions}>
                                                <div style={styles.commentRating}>
                          <span style={comment.score >= 1 ? styles.commentStarFilled : styles.commentStarEmpty}>
                            ★
                          </span>
                                                    <span style={styles.commentScore}>{comment.score}</span>
                                                </div>
                                                {/* 좋아요 버튼 및 카운트 컨테이너 */}
                                                <div style={styles.likeContainer}>
                                                    <button
                                                        style={styles.likeButton}
                                                        onClick={() => handleLikeClick(comment, comment.bookCommentId, comment.isLiked)}
                                                    >
                                                        {comment.isLiked ? <FaHeart style={styles.likedIcon} /> : <FaRegHeart style={styles.likeIcon} />}

                                                    </button>
                                                    {/* 좋아요 카운트 */}
                                                    <span style={styles.likeCountContainer}>{comment.commentLikeCount}</span>
                                                </div>
                                            </div>
                                        </div>
                                        {/* 코멘트 내용 */}
                                        <div style={styles.commentContent}>
                                            <FaComment style={styles.commentIcon} />
                                            <p style={styles.commentText}>{comment.comment}</p>
                                        </div>
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
                                        <button style={styles.moreButton} onClick={handleShowLessComments}>
                                            더보기 취소
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>

                        <div style={styles.section}>
                            <div style={styles.sectionTitle}>관련 도서</div>
                            <div style={styles.sectionContent}>
                                {bookData.relatedBooks &&
                                    bookData.relatedBooks.map((book) => (
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
    image: {
        width: '100%', // 불러오는 div의 100% 크기로 설정
        height: 'auto', // 비율에 맞게 자동 조정
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
};

export default BookDetailPage;