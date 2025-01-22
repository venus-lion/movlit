import React, {useEffect, useState, useMemo} from 'react';
import axios from 'axios';
import axiosInstance from "../../axiosInstance.js"; // axios 임포트
const ChatList = ({activeTab, searchTerm, onSelectChat}) => {

    const [groupChats, setGroupChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [personalChats, setPersonalChats] = useState([]);

    // `filteredChats`를 메모이제이션
    const filteredChats = useMemo(() => {
        const chats = activeTab === 'personal' ? personalChats : groupChats;
        if (activeTab === 'personal') {
            return chats.filter(chat =>
                chat.receiverNickname?.toLowerCase().includes(searchTerm.toLowerCase())
            );
        } else {
            return chats.filter(chat =>
                chat.roomName?.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

    }, [activeTab, searchTerm, personalChats, groupChats]);

    // const chats = activeTab === 'personal' ? personalChats : groupChats;
    //
    // // 검색어 필터링
    // const filteredChats = chats.filter((chat) =>
    //     chat.roomName && chat.roomName.toLowerCase().includes(searchTerm.toLowerCase()) // undefined 체크
    // );


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

    // 나의 일대일 채팅 리스트
    useEffect(() => {
        const fetchPersonalChats = async () => {
            try {
                const response = await axiosInstance.get('/chat/oneOnOne');
                setPersonalChats(response.data); // API에서 받은 데이터로 상태 업데이트
            } catch (error) {
                setError(error.message || '네트워크 오류가 발생했습니다.'); // 오류 처리
            } finally {
                setLoading(false); // 로딩 상태 종료
            }
        };
        fetchPersonalChats();
    }, []); // 컴포넌트가 처음 마운트될 때만 호출

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div>오류: {error}</div>;
    
    return (
        <div style={style.chatListContainer}>
            {/* groups일 때 */}
            {activeTab === 'group' && (
                <div>
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
                            <div style={style.conTitle}>
                                콘텐츠 : <strong>{chat.contentName}</strong>
                            </div>
                            <div style={{fontSize: '0.6em', color: '#aaa'}}>{chat.regDt}</div>
                        </div>
                    ))}

                </div>
            )}

            {/* personal일 때 */}
            {activeTab === 'personal' && (
                <div>
                    {filteredChats.map((chat) => (
                        <div
                            key={chat.oneOnOneChatroomId}
                            style={{
                                padding: '15px',
                                marginBottom: '10px',
                                backgroundColor: '#f9f9f9',
                                border: '1px solid #ccc',
                                borderRadius: '5px',
                                cursor: 'pointer',
                            }}
                            onClick={() => onSelectChat(chat)}
                        >
                            <div style={{fontWeight: 'bold', fontSize: '1.2em'}}>{chat.receiverNickname}</div>
                            <div style={{fontSize: '0.8em', color: '#666'}}>
                                메시지: {chat.latestMessage}
                            </div>
                            <div style={{fontSize: '0.6em', color: '#aaa'}}>{chat.regDt}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );


};

export default ChatList;

