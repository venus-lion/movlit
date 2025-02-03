import React, {useEffect, useRef, useState} from 'react';
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosInstance from '../axiosInstance'; // axios 인스턴스 import
import './ChatPage.css'; // CSS 파일 import
import {useOutletContext} from 'react-router-dom';
import {FaUserCircle} from "react-icons/fa";

function ChatPage({roomId, roomInfo}) {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [hasJoinedRoom, setHasJoinedRoom] = useState(false); // 채팅방 참여 이력
    const [message, setMessage] = useState(''); // 공지 메시지 표시
    const focusDivRef = useRef(null); // 포커스를 주기 위한 ref
    const messagesEndRef = useRef(null);
    // const {isLoggedIn} = useOutletContext();  // 로그인 상태
    const [currentUserId, setCurrentUserId] = useState(null);
    const [isComposing, setIsComposing] = useState(false);
    console.log('roomInfo: ', roomInfo);
    useEffect(() => {
        axiosInstance
            .get(`/members/id`)
            .then((response) => {
                setCurrentUserId(response.data.memberId);
                console.log('현재 로그인 유저: ', currentUserId);
            })
            .catch((error) => {
                console.error('Error fetching current user ID:', error);
            });
    }, []);

    // WebSocket 연결 설정
    useEffect(() => {
        if (!roomId) return; // roomId가 없으면 연결하지 않음

        const client = new Client({
            webSocketFactory: () => new SockJS(`${process.env.VITE_BASE_URL_FOR_CONF}/ws-stomp`),
            connectHeaders: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
            },
            debug: (str) => {
                //console.log('STOMP Debug:', str);
                console.log("메시지  : " + JSON.stringify(messages, null, 2));
            },
        });


        client.onConnect = () => {
            console.log('WebSocket Connected');
            // TODO: GroupChatPage에서는 group으로 처리
            client.subscribe(`/topic/chat/message/one-on-one/${roomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });

            // 과거 메시지 로드
            axiosInstance
                .get(`/chat/history?roomId=${roomId}`)
                .then((response) => {
                    setMessages(response.data);
                })
                .catch((error) => {
                    console.error('Error fetching chat history:', error);
                });
        };

        client.activate();
        setStompClient(client);

        return () => {
            if (client.connected) client.deactivate();
        };
    }, [roomId]); // roomId가 변경될 때마다 재연결

    const sendMessage = () => {
        if (stompClient && newMessage && currentUserId) {
            const chatMessage = {
                roomId: roomId, // roomId 사용
                senderId: currentUserId, // 현재 사용자 ID (실제로는 인증 정보에서 가져와야 함)
                message: newMessage,
                regDt: new Date(),
            };

            stompClient.publish({
                destination: '/app/chat/message/one-on-one',
                body: JSON.stringify(chatMessage),
            });

            setNewMessage('');
        }
    };

    // 크롬 엔터로 채팅할 시 한글 끝문자 처리
    const handleCompositionStart = () => setIsComposing(true);
    const handleCompositionEnd = () => setIsComposing(false);

    // Enter 키로 메시지 보내기
    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !isComposing) {
            sendMessage();
        }
    };

    // 새 메시지가 추가될 때마다 스크롤을 맨 아래로 이동
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({behavior: 'smooth'});
    }, [messages]);

    return (
        <div className="chat-container" style={{display: 'flex', flexDirection: 'column', height: '100%'}}>
            <div className="chat-header">
                <h2>채팅방: {roomInfo.receiverNickname}</h2>
            </div>
            <div style={{flex: 1, overflowY: 'auto', marginBottom: '10px'}}>
                {messages.map((message, index) => {
                    const receiver = {
                        memberId: roomInfo.receiverId,
                        nickname: roomInfo.receiverNickname,
                        profileImgUrl: roomInfo.receiverProfileImgUrl
                    };
                    const isCurrentUser = message.senderId === currentUserId;
                    return (
                        <div
                            key={index}
                            style={{
                                display: 'flex',
                                alignItems: 'flex-start',
                                marginBottom: '10px',
                                flexDirection: isCurrentUser ? 'row-reverse' : 'row',
                            }}
                            className={`message ${isCurrentUser ? 'own-message' : ''}`}
                        >
                            {!isCurrentUser && receiver && (
                                <div className="message-profile">
                                    {/* profileImgUrl이 있으면 이미지를 표시하고, 없으면 FaUserCircle 아이콘을 표시합니다. */}
                                    {receiver.profileImgUrl ? (
                                        <img
                                            src={receiver.profileImgUrl}
                                            alt="Profile"
                                            className="profile-img"
                                        />
                                    ) : (
                                        <FaUserCircle size={36} className="profile-img"/>
                                    )}
                                    <strong>{receiver.nickname}</strong>
                                </div>
                            )}
                            <div className="message-content">
                                <div className={`message-bubble ${isCurrentUser ? 'own-bubble' : ''}`}>
                                    {message.message}
                                </div>
                                <div className="message-time">
                                    {new Date(message.regDt).toLocaleTimeString()}
                                </div>
                            </div>
                        </div>
                    );
                })}
                <div ref={messagesEndRef}/>
                {/* 스크롤을 위한 빈 div */}
            </div>
            <div className="chat-input" style={{display: 'flex', borderTop: '1px solid #ddd', padding: '10px'}}>
                <input
                    style={{flex: 1, padding: '10px', border: '1px solid #ddd'}}
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyDown={handleKeyDown}
                    onCompositionStart={handleCompositionStart}
                    onCompositionEnd={handleCompositionEnd}
                    placeholder="메시지를 입력하세요..."
                />
                <button onClick={sendMessage}>보내기</button>
            </div>
        </div>
    );
}

export default ChatPage;