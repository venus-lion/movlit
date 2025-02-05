import React, {useEffect, useMemo, useState} from 'react';
import axiosInstance from '../../axiosInstance.js';
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const ChatList = ({activeTab, searchTerm, onSelectChat}) => {
    const [groupChats, setGroupChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [personalChats, setPersonalChats] = useState([]);
    const [stompClient, setStompClient] = useState(null);


    // 그룹 채팅방 목록 가져오기 함수 (분리됨)
    const fetchGroupChats = async () => {
        try {
            const response = await axiosInstance.get('/chat/group/rooms/my');
            //console.log('groupchats : ' + response.data);
            setGroupChats(response.data);
        } catch (error) {
            console.error('Error fetching group chats:', error);
        }
    };

    // 일대일 채팅방 목록 가져오기 함수
    const fetchPersonalChats = async () => {
        try {
            const response = await axiosInstance.get('/chat/oneOnOne');
            setPersonalChats(response.data);
            //console.log(response.data);
        } catch (error) {
            setError(error.message || '네트워크 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };


    useEffect(() => {
        // 그룹 채팅방 목록 가져오기
        fetchGroupChats();

        // 일대일 채팅방 목록 가져오기
        fetchPersonalChats();


        // WebSocket 클라이언트 설정
        const client = new Client({
            webSocketFactory: () =>
                new SockJS(`${process.env.VITE_BASE_URL_FOR_CONF}/ws-stomp`),
            connectHeaders: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
            },
            debug: (str) => {
                console.log('STOMP Debug:', str);
            },
        });

        // 연결 시 호출되는 콜백
        client.onConnect = () => {
            console.log('WebSocket Connected');

            // 한 번만 실행되도록 수정 (최초 연결 + 구독 정보 없음)
            if (stompClient && !stompClient.subscriptions) {
                stompClient.subscriptions = {};

                groupChats.forEach((chat) => {
                    const subId = `/topic/chat/message/group/${chat.groupChatroomId}`;
                    if (!stompClient.subscriptions[subId]) {
                        stompClient.subscriptions[subId] = client.subscribe(
                            subId,
                            (message) => {
                                const receivedMessage = JSON.parse(message.body);
                                setGroupChats((prevChats) => {
                                    return prevChats.map((c) =>
                                        c.groupChatroomId === receivedMessage.roomId
                                            ? {...c, recentMessage: receivedMessage}
                                            : c
                                    );
                                });
                            }
                        );
                    }
                });
            }
        };

        client.activate();
        setStompClient(client);

        return () => {
            if (stompClient) {
                // 언마운트 시 모든 구독 해제
                Object.values(stompClient.subscriptions || {}).forEach((sub) =>
                    sub.unsubscribe()
                );
                if (stompClient.connected) stompClient.deactivate();
            }
        };
    }, []); // 의존성 배열 비움


    // 필터링된 채팅 목록 (메모이제이션)
    const filteredChats = useMemo(() => {
        const chats = activeTab === 'personal' ? personalChats : groupChats;
        if (activeTab === 'personal') {
            return chats.filter((chat) =>
                chat.receiverNickname
                    ?.toLowerCase()
                    .includes(searchTerm.toLowerCase())
            );
        } else {
            return chats.filter((chat) =>
                chat.roomName?.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }
    }, [activeTab, searchTerm, personalChats, groupChats]);

    // 스타일 객체
    const style = {
        conTitle: {
            fontSize: '0.9em',
            color: 'black',
            width: '70%',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
        },
        recentMsg: {
            fontSize: '0.9em',
            color: '#666',
            backgroundColor: '#faf9d7',
            padding: '5px',
            width: '100%',
            borderRadius: '10px',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
        },
        chatListContainer: {
            maxHeight: '550px',
            overflowY: 'auto',
            border: '1px solid #ddd',
            padding: '10px',
            borderRadius: '4px',
        },
    };

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div>오류: {error}</div>;

    return (
        <div style={style.chatListContainer}>
            {/* 그룹 채팅 목록 */}
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
                            <div style={{fontWeight: 'bold', color: 'black'}}>
                                {chat.roomName}
                            </div>
                            <div style={style.conTitle}>
                                [콘텐츠] <strong>{chat.contentName}</strong>
                            </div>
                            <div>
                                {chat.recentMessage ? (
                                    <div>
                                        <div style={style.recentMsg}>
                                            <strong>[New] </strong>
                                            {chat.recentMessage.message}
                                            <br/>
                                            {chat.recentMessage.regDt}
                                        </div>
                                    </div>
                                ) : (
                                    <div>
                                        <div style={style.recentMsg}>메시지 없음</div>
                                    </div>
                                )}
                                <div style={{fontSize: '0.6em', color: '#aaa'}}>
                                    {chat.regDt}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* 일대일 채팅 목록 */}
            {activeTab === 'personal' && (
                <div>
                    {filteredChats.map((chat) => (
                        <div
                            key={chat.roomId}
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
                            <div style={{fontWeight: 'bold', fontSize: '1.2em'}}>
                                {chat.receiverNickname}
                            </div>
                            <div style={{fontSize: '0.8em', color: '#666'}}>
                                메시지: {chat.latestMessage}
                            </div>
                            <div style={{fontSize: '0.6em', color: '#aaa'}}>
                                {chat.regDt}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ChatList;