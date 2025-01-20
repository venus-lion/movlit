import React, {useState} from "react";
import Modal from "react-modal";
import {FaStar, FaRegStar, FaStarHalfAlt} from 'react-icons/fa';
import "../../assets/css/ModalStyle.css";
import axiosInstance from "../../axiosInstance.js";

// 별을 표시하는 함수
const renderStars = (rating) => {
    // rating 값을 0 ~ 10으로 받을 경우
    const validRating = Math.max(0, Math.min(10, rating || 0));  // 0 ~ 10 사이로 제한

    // 2점마다 1개의 꽉 찬 별로 환산
    const fullStars = Math.floor(validRating / 2);  // 꽉 찬 별 개수
    const halfStar = validRating % 2 >= 1 ? 1 : 0;  // 반쪽 별 여부 (나머지가 1 이상이면 반쪽 별)
    const emptyStars = 5 - fullStars - halfStar;  // 빈 별 개수 (총 5개 별이므로 나머지)

    return (
        <>
            {[...Array(fullStars)].map((_, index) => <FaStar key={`full-${index}`} className="star-icon"/>)}
            {halfStar === 1 && <FaStarHalfAlt className="star-icon"/>}
            {[...Array(emptyStars)].map((_, index) => <FaRegStar key={`empty-${index}`} className="star-icon"/>)}
        </>
    );
};

// 제목 축약 함수
const truncateTitle = (title, maxLength = 15) => {
    if (title.length > maxLength) {
        return `${title.slice(0, maxLength)}...`;
    }
    return title;
};

const CreateGroupChatModal = ({isOpen, onClose}) => {
    const [selectedCategory, setSelectedCategory] = useState("movie"); // 기본 선택 영화
    const [searchTerm, setSearchTerm] = useState(""); // 검색어
    const [searchResults, setSearchResults] = useState([]); // 검색 결과

    // 라디오 버튼 변경 핸들러
    const handleCategoryChange = (event) => {
        setSearchResults([]);
        setSelectedCategory(event.target.value);
    };

    // 검색 버튼 클릭 핸들러
    const handleSearch = async () => {
        if (!searchTerm.trim()) {
            alert("검색어를 입력하세요.");
            return;
        }
        setSearchResults([]);

        // TODO: 실제 API 호출로 검색 결과 가져오기
        try {
            if (selectedCategory === "movie") {
                //  영화 데이터 가져오기
                const response = await axiosInstance.get(`/movies/search/searchMovie`, {
                    params: {
                        page: 1,
                        pageSize: 20,
                        inputStr: searchTerm
                    },
                });

                const movieData = await response.data.movieList;
                setSearchResults(movieData); // 결과가 없으면 빈 배열로 설정
            } else if (selectedCategory === "book") {
                // 도서 데이터 가져오기
                const response = await axiosInstance.get(`/books/search/searchBook`, {
                    params: {
                        page: 1,
                        pageSize: 20,
                        inputStr: searchTerm
                    }
                });
                const bookData = await response.data.bookESVoList;
                setSearchResults(bookData);

                console.log(bookData);
            }


        } catch (error) {
            console.error("Error fetching search results:", error);
            setSearchResults([]); // 오류 발생 시 빈 배열
        }
    };

    // 취소 버튼 클릭 핸들러
    const handleCancel = () => {
        setSearchTerm(""); // 검색어 초기화
        setSearchResults([]); // 검색 결과 초기화
        onClose(); // 모달 닫기
    };

    // 초기화 및 모달 닫기 핸들러
    const handleClose = () => {
        setSearchTerm(""); // 검색어 초기화
        setSearchResults([]); // 검색 결과 초기화
        onClose(); // 모달 닫기
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={handleClose}
            className="custom-modal"
            overlayClassName="custom-overlay"
            ariaHideApp={false}
        >
            <div className="modal-content">
                {/* 모달 헤더 */}
                <div className="modal-header">
                    <h2>채팅방 생성</h2>
                </div>

                {/* 모달 본문 */}
                <div className="modal-body">
                    {/* 카테고리 선택 */}
                    <div className="modal-tab-container">
                        <label className="radio-label">
                            <input
                                type="radio"
                                name="category"
                                value="movie"
                                checked={selectedCategory === "movie"}
                                onChange={handleCategoryChange}
                                className="radio radio-item"
                            />
                            영화
                        </label>
                        <label className="radio-label">
                            <input
                                type="radio"
                                name="category"
                                value="book"
                                checked={selectedCategory === "book"}
                                onChange={handleCategoryChange}
                                className="radio radio-item"
                            />
                            책
                        </label>
                    </div>

                    {/* 검색 영역 */}
                    <div className="search-container">
                        <input
                            type="text"
                            placeholder="검색어를 입력하세요"
                            className="search-input"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <button className="search-button" onClick={handleSearch}>
                            검색
                        </button>
                    </div>

                    {/* 검색 결과 */}
                    <div className="result-container">
                        <h3>검색 결과</h3>
                        <div className="results-scroll">
                            {searchResults.length > 0 ? (
                                <div className="results-grid">
                                    {searchResults.map((result, index) => (
                                        <div key={index} className="result-card">
                                            {selectedCategory === "movie" ? (
                                                <>
                                                    <img src={result.posterPath} alt={result.title}
                                                         className="result-image"/>
                                                    <div className="result-title">{truncateTitle(result.title)}</div>
                                                    <div
                                                        className="result-rating">
                                                        ⭐<span>({Math.round(parseFloat(result.voteAverage) * 10) / 10})</span>
                                                    </div>
                                                    <div>
                                                        <p className="result-info">
                                                            {result.movieGenre.map((g) => g.genreName).join(', ')}
                                                        </p>
                                                    </div>
                                                </>
                                            ) : (
                                                <>
                                                    <img src={result.bookImgUrl} alt={result.title}
                                                         className="result-image"/>
                                                    <div className="result-title">{truncateTitle(result.title)}</div>
                                                    <div>
                                                        <p className="result-info">
                                                            {result.crew.join(', ')}
                                                        </p>
                                                    </div>
                                                </>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p>검색결과가 없습니다.</p>
                            )}
                        </div>
                    </div>
                </div>

                {/* 모달 푸터 */}
                <div className="modal-footer">
                    <button className="modal-cancel" onClick={handleCancel}>
                        취소
                    </button>
                    <button className="modal-confirm">선택</button>
                </div>
            </div>
        </Modal>
    );
};

export default CreateGroupChatModal;
