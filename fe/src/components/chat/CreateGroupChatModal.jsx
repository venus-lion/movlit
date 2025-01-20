import React, {useState} from "react";
import Modal from "react-modal";
import * as RadioGroup from "@radix-ui/react-radio-group";
import "../../assets/css/ModalStyle.css";

const CreateGroupChatModal = ({isOpen, onClose}) => {
    const [selectedCategory, setSelectedCategory] = useState("movie"); // 기본 선택 영화
    const [searchTerm, setSearchTerm] = useState(""); // 검색어
    const [searchResults, setSearchResults] = useState([]); // 검색 결과

    // 라디오 버튼 변경 핸들러
    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
    };

    // 검색 버튼 클릭 핸들러
    const handleSearch = async () => {
        if (!searchTerm.trim()) {
            alert("검색어를 입력하세요.");
            return;
        }

        // TODO: 실제 API 호출로 검색 결과 가져오기
        try {
            const response = await fetch(
                `/api/search?category=${selectedCategory}&query=${encodeURIComponent(
                    searchTerm
                )}`
            );
            const data = await response.json();

            setSearchResults(data.results || []); // 결과가 없으면 빈 배열로 설정
        } catch (error) {
            console.error("Error fetching search results:", error);
            setSearchResults([]); // 오류 발생 시 빈 배열
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
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
                        {/*<RadioGroup.Root*/}
                        {/*    className="radio-group"*/}
                        {/*    value={selectedCategory}*/}
                        {/*    onValueChange={setSelectedCategory}*/}
                        {/*>*/}
                        {/*    <div className="radio-item">*/}
                        {/*        <RadioGroup.Item*/}
                        {/*            value="movie"*/}
                        {/*            className="radio"*/}
                        {/*            id="movie"*/}
                        {/*        />*/}
                        {/*        <label htmlFor="movie" className="radio-label">*/}
                        {/*            영화*/}
                        {/*        </label>*/}
                        {/*    </div>*/}
                        {/*    <div className="radio-item">*/}
                        {/*        <RadioGroup.Item*/}
                        {/*            value="book"*/}
                        {/*            className="radio"*/}
                        {/*            id="book"*/}
                        {/*        />*/}
                        {/*        <label htmlFor="book" className="radio-label">*/}
                        {/*            책*/}
                        {/*        </label>*/}
                        {/*    </div>*/}
                        {/*</RadioGroup.Root>*/}
                        <label>
                            <input
                                type="radio"
                                name="category"
                                value="movie"
                                checked={selectedCategory === "movie"}
                                onChange={handleCategoryChange}
                            />
                            영화
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="category"
                                value="book"
                                checked={selectedCategory === "book"}
                                onChange={handleCategoryChange}
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
                        <button className="modal-search-button" onClick={handleSearch}>
                            검색
                        </button>
                    </div>

                    {/* 검색 결과 */}
                    <div className="result-container">
                        <h3>검색 결과</h3>
                        {searchResults.length > 0 ? (
                            <ul>
                                {searchResults.map((result, index) => (
                                    <li key={index}>{result}</li>
                                ))}
                            </ul>
                        ) : (
                            <p>검색결과가 없습니다.</p>
                        )}
                    </div>
                </div>

                {/* 모달 푸터 */}
                <div className="modal-footer">
                    <button className="modal-cancel" onClick={onClose}>
                        취소
                    </button>
                    <button className="modal-confirm">선택</button>
                </div>
            </div>
        </Modal>
    );
};

export default CreateGroupChatModal;
