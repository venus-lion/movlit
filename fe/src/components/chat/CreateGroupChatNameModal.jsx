import React, {useState} from "react";
import Modal from "react-modal";
import {FaRegStar, FaStar, FaStarHalfAlt} from 'react-icons/fa';
import "../../assets/css/CreateGroupChatNameModal.css";
import axiosInstance from "../../axiosInstance.js";
import {toast} from "react-toastify";

// 별을 표시하는 함수
const renderStars = (rating) => {
    // rating 값을 0 ~ 10으로 받을 경우
    const validRating = Math.max(0, Math.min(10, rating || 0)); // 0 ~ 10 사이로 제한
    const fullStars = Math.floor(validRating / 2); // 꽉 찬 별 개수
    const halfStar = validRating % 2 >= 1 ? 1 : 0; // 반쪽 별 여부 (나머지가 1 이상이면 반쪽 별)
    const emptyStars = 5 - fullStars - halfStar; // 반쪽 별 여부 (나머지가 1 이상이면 반쪽 별)

    return (
        <>
            {[...Array(fullStars)].map((_, index) => <FaStar key={`full-${index}`} className="star-icon"/>)}
            {halfStar === 1 && <FaStarHalfAlt className="star-icon"/>}
            {[...Array(emptyStars)].map((_, index) => <FaRegStar key={`empty-${index}`} className="star-icon"/>)}
        </>
    );
};

const CreateGroupChatNameModal = ({isOpen, onClose, selectedCard, selectedCategory, onUpdateChatList}) => {
    if (!selectedCard) return null;

    const [chatroomName, setChatroomName] = useState("");

    const handleInputChange = (event) => {
        setChatroomName(event.target.value);
    };

    const handleSubmit = async () => {
        if (!chatroomName.trim()) {
            toast.error('채팅방 제목은 필수입니다.');
            return;
        }

        // GroupChatroomRequest에 매핑되는 데이터
        const requestData = {
            roomName: chatroomName,
            contentType: selectedCategory,
            contentId: selectedCategory === "movie" ? selectedCard.movieId : selectedCard.bookId,
        };

        try {
            const response = await axiosInstance.post("/chat/create/group", requestData);
            console.log("채팅방 생성 성공:", response.data);
            toast.success('채팅방을 생성했습니다.');
            onUpdateChatList();
            onClose();
        } catch (error) {
            toast.success('채팅방 생성에 실패했습니다.');
        }
    };

    return (
        <Modal isOpen={isOpen} onRequestClose={onClose} className="custom-modal-2" overlayClassName="custom-overlay"
               ariaHideApp={false}>
            <div className="modal-2-content">
                <div className="modal-header">
                    <h2>채팅방 이름 설정</h2>
                </div>
                <div className="modal-2-body">
                    {selectedCategory === "movie" ? (
                        <>
                            <img src={selectedCard.posterPath} alt={selectedCard.title} className="selected-image"/>
                            <div>{selectedCard.title}</div>
                            <div className="selected-rating">
                                {renderStars(selectedCard.voteAverage)}
                            </div>
                            <div>
                                <p className="selected-info">
                                    {selectedCard.movieGenre.map((g) => g.genreName).join(', ')}
                                </p>
                            </div>
                        </>
                    ) : (
                        <>
                            <img src={selectedCard.bookImgUrl} alt={selectedCard.title} className="selected-image"/>
                            <div>{selectedCard.title}</div>
                            <div>
                                <p className="selected-info">
                                    {selectedCard.crew.join(', ')}
                                </p>
                            </div>
                        </>
                    )}
                </div>
                <div className="input-container">
                    <input
                        type="text"
                        className="chatroom-name-input"
                        placeholder="채팅방 제목을 입력해주세요"
                        value={chatroomName}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="modal-footer">
                    <button className="cancel-button" onClick={onClose}>
                        취소
                    </button>
                    <button onClick={handleSubmit}>
                        생성
                    </button>
                </div>
            </div>
        </Modal>
    );
};

export default CreateGroupChatNameModal;