import React, { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosInstance from '../axiosInstance'; // axios 인스턴스 import
import './ChatPage.css'; // CSS 파일 import

function ChatPageGroup({ roomId }) {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [members, setMembers] = useState([]);
    const messagesEndRef = useRef(null);

    // 현재 로그인한 userId
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

    //const currentUserId = '868f8146000000ed6e44cef9'; // 임시 현재 로그인 사용자 ID (test 유저)

    // 그룹 채팅방 멤버 목록 불러오기
    useEffect(() => {
        if (roomId) {
            axiosInstance
                .get(`/chat/${roomId}/members`)
                .then((response) => {
                    setMembers(response.data);

                    // 잘 불러오는지 확인
                    console.log('fetched members :: (response.data) : ', response.data);

                    // 각 멤버 정보 콘솔 출력
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

    // WebSocket 연결 설정 및 메시지 수신
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
            });

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
        if (stompClient && newMessage && currentUserId) { // currentUserId가 null이 아닐 때만 전송
            const chatMessage = {
                roomId: roomId,
                senderId: currentUserId, // 현재 로그인한 유저가 senderId가 됨
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

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') sendMessage();
    };

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <div className="chat-container" style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div className="chat-header">
                <h2>채팅방: {roomId}</h2>
                {/*<div className="members-list">*/}
                {/*    <h4>참여 멤버:</h4>*/}
                {/*    {members.map((member) => (*/}
                {/*        <div key={member.memberId} className="member">*/}
                {/*            <img*/}
                {/*                src={member.profileImgUrl}*/}
                {/*                alt="Profile"*/}
                {/*                className="profile-img"*/}
                {/*            />*/}
                {/*            <span>{member.nickname}</span>*/}
                {/*        </div>*/}
                {/*    ))}*/}
                {/*</div>*/}
            </div>
            <div style={{ flex: 1, overflowY: 'auto', marginBottom: '10px' }}>
                {messages.map((message, index) => {
                    // 그룹채팅방에 속한 다른 멤버 뽑아오기
                    let member = null;
                    if (members.length > 1) { // 단체채팅방에 2명 이상이라면...
                        member = members.find((m) => m.memberId !== currentUserId);
                    }
                    //const member = members.find((m) => m.memberId !== currentUserId);
                    const isCurrentUser = message.senderId === currentUserId;

                    // console.log("다른 멤버 ::: " + member.nickname);
                    // console.log("현재 로그인한 멤버 ::: " + isCurrentUser);

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
                            {!isCurrentUser && member && ( // 다른 멤버가 존재할 때만 프로필 표시
                                <div className="message-profile">
                                    <img
                                        src={member?.profileImgUrl}
                                        alt="Profile"
                                        className="profile-img"
                                    />
                                    <strong>{member?.nickname}</strong>
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
                <div ref={messagesEndRef} />
            </div>
            <div className="chat-input-container">
                <input
                    type="text"
                    className="chat-input"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="메시지를 입력하세요..."
                />
                <button onClick={sendMessage}>보내기</button>
            </div>
        </div>
    );
}

export default ChatPageGroup;