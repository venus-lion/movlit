import React, {useState} from 'react';
import ChatTabs from './ChatTabs';
import ChatList from './ChatList';
import {useNavigate, useOutletContext} from "react-router-dom";
import ChatPage from '../../pages/ChatPage.jsx';
import CreateGroupChatModal from "./CreateGroupChatModal.jsx";
import ChatPageGroup from "../../pages/ChatPageGroup.jsx";
import CreateGroupChatNameModal from "./CreateGroupChatNameModal.jsx";



const Chat = () => {
    const [activeTab, setActiveTab] = useState('personal'); // 개인 채팅 또는 그룹 채팅
    const [searchTerm, setSearchTerm] = useState(''); // 검색어
    const [selectedChat, setSelectedChat] = useState(null); // 현재 선택된 채팅방
    const {isLoggedIn} = useOutletContext();
    const navigate = useNavigate();

    const [isCreateGroupChatModalOpen, setIsCreateGroupChatModalOpen] = useState(false); // 모달1 열림 상태
    const [isCreateGroupChatNameModalOpen, setIsCreateGroupChatNameModalOpen] = useState(false); // 모달2 열림 상태
    const [selectedCard, setSelectedCard] = useState(null); // 선택된 데이터
    const [selectedCategory, setSelectedCategory] = useState(null);

    const handleCreateGroupChatModal = () => {
        setIsCreateGroupChatModalOpen(true);
    };

    const handleCloseGroupChatModal = () => {
        setIsCreateGroupChatModalOpen(false);
        setSelectedCard(null);
        setSelectedCategory(null);
    };

    const handleOpenGroupChatNameModal = (card, category) => {
        setSelectedCard(card); // 선택된 카드 데이터 저장
        setSelectedCategory(category); // 선택된 카테고리 저장
        setIsCreateGroupChatModalOpen(false); // 첫 번째 모달 닫기
        setIsCreateGroupChatNameModalOpen(true); // 두 번째 모달 열기
    };

    const handleCloseGroupChatNameModal = () => {
        setIsCreateGroupChatNameModalOpen(false);
        setSelectedCard(null);
        setSelectedCategory(null);
    };

    // 검색 핸들러
    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    // 채팅방 생성 핸들러
    const handleCreateChat = () => {
        if (!isLoggedIn) { // 로그인을 하지 않았다면..
            alert('로그인이 필요합니다.');
            navigate('/member/login');
            return;
        }
        setIsCreateGroupChatModalOpen(true); // 모달 열기

        // const roomName = prompt('생성할 채팅방 이름을 입력하세요 : ');
        // if (!roomName) return;

        /*
            TODO : 이미 존재하는 채팅방 제목이면, ‘이미 존재하는 채팅방입니다. 참여하시겠습니까?’ 하고 팝업창 띄우고
              ‘참여’ 버튼 누르면, 이미 존재하는 채팅방 참여하는 것
         */

        // 채팅방이 존재하지 않으면 새로운 채팅방 개설
        // -- 근데 일대일채팅방인지, 그룹채팅방인지에 따라 다른 생성 api를 호출해야 하고..
        // -- 그리고 일대일채팅이면,

    };

    return (
        <div style={{display: 'flex', height: 'calc(100vh - 60px)'}}>
            {/* 왼쪽: 채팅 목록 */}
            <div style={{
                width: '30%',
                borderRight: '1px solid #ddd',
                padding: '10px',
                display: 'flex',
                flexDirection: 'column',
                position: 'relative' // 상대적 위치 설정
            }}>
                <ChatTabs activeTab={activeTab} setActiveTab={setActiveTab}/>
                <input
                    type="text"
                    placeholder={activeTab === 'personal' ? '개인 채팅 검색' : '그룹 채팅 검색'}
                    value={searchTerm}
                    onChange={handleSearch}
                    style={{
                        width: '100%',
                        padding: '10px',
                        marginBottom: '10px',
                        border: '1px solid #ddd',
                        borderRadius: '4px',
                    }}
                />
                <ChatList
                    activeTab={activeTab}
                    searchTerm={searchTerm}
                    onSelectChat={(chat) => setSelectedChat(chat)} // 선택된 채팅방 설정
                />

                {/* 채팅방 생성 버튼 */}
                {activeTab === "group" && (

                    <button
                        onClick={handleCreateGroupChatModal}
                        style={{
                            backgroundColor: 'green',
                            color: 'white',
                            borderRadius: '15px',
                            width: '50px',
                            height: '50px',
                            aspectRatio: '1 / 1', // 가로 세로 비율 유지 (원형 유지)
                            fontSize: '30px',
                            cursor: 'pointer',
                            boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
                            position: 'absolute', // 사이드바 내부에서 절대 위치 설정
                            bottom: '40px', // 하단 여백
                            right: '15px', // 오른쪽 정렬
                            display: 'flex',   // 중앙 정렬을 위한 flex 적용
                            alignItems: 'center',
                            justifyContent: 'center',
                            flexShrink: 0,  // 부모 flex 영향을 받지 않도록 고정
                            overflow: 'hidden' // 내부 요소 넘침 방지
                        }}
                    >
                        +
                    </button>
                )}
            </div>

            {/* 오른쪽: 채팅 화면 */}
            {/*<div style={{flex: 1, padding: '10px'}}>*/}
            {/*    { selectedChat ? (*/}
            {/*        <ChatPage roomId={selectedChat.id}/> /* 선택된 채팅방 ID 전달 */}
            {/*    ) : (*/}
            {/*        <div style={{textAlign: 'center', marginTop: '20%'}}>*/}
            {/*            채팅방을 선택해주세요.*/}
            {/*        </div>*/}
            {/*    )}*/}
            {/*</div>*/}

            <div style={{flex: 1, padding: '10px'}}>
                {selectedChat ? (
                    activeTab === 'personal' ? (
                        <ChatPage roomId={selectedChat.id}/> /* 개인 채팅방 */
                    ) : (
                        <ChatPageGroup roomId={selectedChat.id}/> /* 그룹 채팅방 */
                    )
                ) : (
                    <div style={{textAlign: 'center', marginTop: '20%'}}>
                        {activeTab === 'personal' ? '개인' : '그룹'} 채팅방을 선택해주세요.
                    </div>
                )}
            </div>

            {/* 모달 창 */}
            <CreateGroupChatModal
                isOpen={isCreateGroupChatModalOpen}
                onClose={handleCloseGroupChatModal}
                onConfirm={(card, category) => handleOpenGroupChatNameModal(card, category)} // 선택된 데이터 전달
            />
            <CreateGroupChatNameModal
                isOpen={isCreateGroupChatNameModalOpen}
                onClose={handleCloseGroupChatNameModal}
                selectedCard={selectedCard} // 선택된 데이터 전달
                selectedCategory={selectedCategory} // 선택된 카테고리 전달
            />
        </div>
    );

};

export default Chat;
