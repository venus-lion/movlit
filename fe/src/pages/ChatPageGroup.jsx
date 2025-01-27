import React, { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosInstance from '../axiosInstance';
import './ChatPageGroup.css';
import { FaUserCircle } from 'react-icons/fa'; // react-icons에서 기본 프로필 이미지 아이콘을 가져옵니다.

function ChatPageGroup({ roomId, roomInfo }) {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [members, setMembers] = useState([]);
    const messagesEndRef = useRef(null);
    const [isComposing, setIsComposing] = useState(false);
    const [currentUserId, setCurrentUserId] = useState(null);

    useEffect(() => {
        axiosInstance
            .get(`/members/id`)
            .then((response) => {
                setCurrentUserId(response.data.memberId);
                console.log('현재 로그인한 사람 :::: ' + response.data.memberId);
            })
            .catch((error) => {
                console.error('Error fetching current user ID:', error);
            });
    }, []);

    useEffect(() => {
        if (roomId) {
            axiosInstance
                .get(`/chat/${roomId}/members`)
                .then((response) => {
                    setMembers(response.data);
                    console.log('fetched members :: (response.data) : ', response.data);
                    response.data.forEach(member => {
                        console.log("Member ID:", member.memberId);
                        console.log("Nickname:", member.nickname);
                        console.log("Profile Image URL:", member.profileImgUrl);
                    });
                })
                .catch((error) => {
                    console.error('Error fetching chatroom members:', error);
                });
        }
    }, [roomId]);

    useEffect(() => {
        if (!roomId) return;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${process.env.VITE_BASE_URL_FOR_CONF}/ws-stomp`),
            connectHeaders: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
            },
        });

        client.onConnect = () => {
            client.subscribe(`/topic/chat/message/group/${roomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);

                // console.log('subscriber 이후 받은 roomId :: ' + roomId);
                // console.log('subscriber 이후 받은 chatMessageDto :: ' + receivedMessage.senderId);
                // console.log('subscriber 이후 받은 message :: ' + receivedMessage.message);
            });

            client.subscribe(`/topic/chat/room/${roomId}`, (message => {
                const updatedMembers = JSON.parse(message.body);

                console.log('구독한 이후, 오는 메세지가 오긴 하니?');
                console.log('Updated members :: ' + updatedMembers);
                console.log('subscriber에서의 groupRoomId :: ' + roomId);

               setMembers(updatedMembers);

            }))

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
    }, [roomId]);

    const sendMessage = () => {
        if (stompClient && newMessage && currentUserId) {
            const chatMessage = {
                roomId: roomId,
                senderId: currentUserId,
                message: newMessage,
                regDt: new Date(),
            };

            stompClient.publish({
                destination: '/app/chat/message/group',
                body: JSON.stringify(chatMessage),
            });

            setNewMessage('');
        }
    };

    const handleCompositionStart = () => setIsComposing(true);
    const handleCompositionEnd = () => setIsComposing(false);

    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !isComposing) {
            sendMessage();
        }
    };

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <div className="chat-container-group" style={{ display: 'flex', flexDirection: 'column', height: '90%' }}>
            <div className="chat-header-group">
                <h2>채팅방: {roomInfo.roomName}</h2>
            </div>
            <div className="chat-messages-group">
                {messages.map((message, index) => {
                    const sender = members.find((m) => m.memberId === message.senderId);
                    const isCurrentUser = message.senderId === currentUserId;

                    return (
                        <div
                            key={index}
                            className={`message-group ${isCurrentUser ? 'own-message-group' : ''}`}
                        >
                            {!isCurrentUser && sender && (
                                <div className="message-profile-group">
                                    {/* profileImgUrl이 있으면 이미지를 표시하고, 없으면 FaUserCircle 아이콘을 표시합니다. */}
                                    {sender && sender.profileImgUrl ? (
                                        <img
                                            src={sender.profileImgUrl}
                                            alt="Profile"
                                            className="profile-img-group"
                                        />
                                    ) : (
                                        <FaUserCircle size={40} className="profile-img" />
                                    )}
                                    <strong>{sender.nickname}</strong>
                                </div>
                            )}
                            <div className="message-content-group">
                                <div className={`message-bubble-group ${isCurrentUser ? 'own-bubble' : ''}`}>
                                    {message.message}
                                </div>
                                <div className="message-time-group">
                                    {new Date(message.regDt).toLocaleTimeString()}
                                </div>
                            </div>
                        </div>
                    );
                })}
                <div ref={messagesEndRef} />
            </div>
            <div className="chat-input-container-group">
                <input
                    type="text"
                    className="chat-input-group"
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

export default ChatPageGroup;