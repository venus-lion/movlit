import React, { useEffect, useState } from 'react';
import axiosInstance from "../../axiosInstance.js"; // axios 임포트

const ChatList = ({ activeTab, searchTerm, onSelectChat }) => {
    const [groupChats, setGroupChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const personalChats = [
        {groupChatroomId: 1450, roomName: '김현경', lastMessage: '안녕하세요!', regDt: '8시간 전'},
        {groupChatroomId: 2879, roomName: '박상원', lastMessage: '회의 일정입니다.', regDt: '2시간 전'},
        {groupChatroomId: '58521ef', roomName: '제임스', lastMessage: '반가워요', regDt: '2시간 전'},
        {groupChatroomId: 12421, roomName: '케이트', lastMessage: '좋은아침', regDt: '2시간 전'},
    ];

    const chats = activeTab === 'personal' ? personalChats : groupChats;

    // 검색어 필터링
    const filteredChats = chats.filter((chat) =>
        chat.roomName && chat.roomName.toLowerCase().includes(searchTerm.toLowerCase()) // undefined 체크
    );

    // 채팅방 리스트 무한 스크롤 구현 전
    // 프론트에서 임시로 전체 리스트 스크롤 구현
    const style = {
        conTitle: {
            fontSize: '0.7em',
            color: '#666',
            width: '70%',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap'
        },
        chatListContainer: {
            maxHeight: '550px', // 리스트의 최대 높이를 설정
            overflowY: 'auto', // 수직 스크롤 가능
            border: '1px solid #ddd', // 경계선 추가
            padding: '10px',
            borderRadius: '4px', // 경계선 둥글게 처리
        }
    }

    // 나의 그룹채팅 리스트
    useEffect(() => {
        const fetchGroupChats = async () => {
            try {
                const response = await axiosInstance.get('/chat/group/myGroupChatrooms');
                console.log(response.data); // 데이터 출력
                setGroupChats(response.data); // API에서 받은 데이터로 상태 업데이트
            } catch (error) {
                setError(error.message || '네트워크 오류가 발생했습니다.'); // 오류 처리
            } finally {
                setLoading(false); // 로딩 상태 종료
            }
        };
        fetchGroupChats();
    }, []); // 컴포넌트가 처음 마운트될 때만 호출

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div>오류: {error}</div>;

    return (
        <div style={style.chatListContainer}>
            {filteredChats.map((chat) => (
                <div
                    key={chat.groupChatroomId}
                    style={{
                        padding: '10px',
                        borderBottom: '1px solid #ddd',
                        cursor: 'pointer',
                    }}
                    onClick={() => onSelectChat(chat)}
                >
                    <div style={{fontWeight: 'bold'}}>{chat.roomName}</div>
                    <div style={style.conTitle}>콘텐츠 : <strong>{chat.contentName}</strong></div>
                    <div style={{fontSize: '0.6em', color: '#aaa'}}>{chat.regDt}</div>
                </div>
            ))}
        </div>
    );
};

export default ChatList;