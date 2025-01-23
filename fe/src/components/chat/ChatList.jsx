import React, {useEffect, useState, useMemo} from 'react';
import axiosInstance from "../../axiosInstance.js"; // axios 임포트
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
const ChatList = ({activeTab, searchTerm, onSelectChat}) => {

    const [groupChats, setGroupChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [personalChats, setPersonalChats] = useState([]);

    /*
     * websocket
     */

    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        const fetchGroupChats = async () => {
            try {
                const response = await axiosInstance.get('/chat/group/myGroupChatrooms');
                console.log('groupchats : ' + response.data);
                setGroupChats(response.data);  // 그룹 채팅 목록 저장
            } catch (error) {
                console.error('Error fetching group chats:', error);
            }
        };

        fetchGroupChats();

        // WebSocket 클라이언트 설정
        const client = new Client({
            webSocketFactory: () => new SockJS(`${process.env.VITE_BASE_URL_FOR_CONF}/ws-stomp`),
            connectHeaders: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
            },
            debug: (str) => {
                console.log('STOMP Debug:', str);
                console.log("그룹채팅방  : " + JSON.stringify(groupChats, null, 2));
            },
        });

        client.onConnect = () => {
            console.log('WebSocket Connected');
            // 각 그룹 채팅방에 대한 메시지 구독
            groupChats.forEach(chat => {
                client.subscribe(`/topic/chat/message/group/${chat.groupChatroomId}`, (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    // 수신한 메시지로 그룹 채팅 목록 업데이트
                    setGroupChats(prevChats => {
                        return prevChats.map(chat =>
                            chat.groupChatroomId === receivedMessage.roomId
                                ? { ...chat, recentMessage: receivedMessage } // 최신 메시지 업데이트
                                : chat
                            );
                    });
                });
            });
        };


        client.activate(); // WebSocket 활성화
        setStompClient(client);

        return () => {
            if (client.connected) client.deactivate(); // 언마운트 시 WebSocket 종료
        };
    }, [groupChats]); // 의존성 배열에 groupChats 추가하여 메시지 수신 구독이 동적으로 업데이트 되도록 함




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

    // 채팅방 리스트 무한 스크롤 구현 전
    // 프론트에서 임시로 전체 리스트 스크롤 구현
    const style = {
        conTitle: {
            fontSize: '0.9em',
            color: 'black',
            width: '70%',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap'
        },
        recentMsg: {
            fontSize: '0.9em',
            color: '#666',
            backgroundColor : '#faf9d7',
            padding: '5px',
            width: '100%',
            borderRadius: '10px',
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
    // useEffect(() => {
    //     const fetchGroupChats = async () => {
    //         try {
    //             const response = await axiosInstance.get('/chat/group/myGroupChatrooms');
    //             console.log(response.data); // 데이터 출력
    //             setGroupChats(response.data); // API에서 받은 데이터로 상태 업데이트
    //         } catch (error) {
    //             setError(error.message || '네트워크 오류가 발생했습니다.'); // 오류 처리
    //         } finally {
    //             setLoading(false); // 로딩 상태 종료
    //         }
    //     };
    //     fetchGroupChats();
    // }, []); // 컴포넌트가 처음 마운트될 때만 호출

    // 나의 일대일 채팅 리스트
    useEffect(() => {
        const fetchPersonalChats = async () => {
            try {
                const response = await axiosInstance.get('/chat/oneOnOne');
                setPersonalChats(response.data); // API에서 받은 데이터로 상태 업데이트
                console.log(response.data);
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
                                cursor: 'pointer'
                            }}
                            onClick={() => onSelectChat(chat)}
                        >
                            <div style={{fontWeight: 'bold', color:'black'}}>{chat.roomName}</div>
                            <div style={style.conTitle}>
                                [콘텐츠] <strong>{chat.contentName}</strong>
                            </div>
                            <div>
                                    {chat.recentMessage ? (
                                        <div>
                                            <div style={style.recentMsg}>
                                                <strong>[New] </strong>{chat.recentMessage.message}<br />
                                                {chat.recentMessage.regDt}
                                            </div>

                                        </div>
                                    ) : (
                                        <div>
                                            {/* 여기에 다른 표시할 내용이나 기본 메시지를 추가할 수 있습니다 */}
                                            <div style={style.recentMsg}>
                                                메시지 없음
                                            </div>
                                        </div>
                                    )}
                                <div style={{ fontSize: '0.6em', color: '#aaa' }}>{chat.regDt}</div>

                            </div>

                        </div>
                    ))}

                </div>
            )}

            {/* personal일 때 */}
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

