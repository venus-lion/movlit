import React, { useState, useEffect, useRef } from 'react';
import axiosInstance from '../axiosInstance';
import './ChatPage.css';

function ChatPageGroup({ roomId }) {
    const [members, setMembers] = useState([]);
    const [messages, setMessages] = useState([]);
    const messagesEndRef = useRef(null);

    const currentUserId = '868f8146000000ed6e44cef9'; // 임시로 현재 로그인한 사용자 ID 설정 (test)

    // 그룹 채팅방 멤버 목록 불러오기
    useEffect(() => {
        const roomId = 'GRP_HP'; // 임시로 roomId 설정

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

    // 임시 메시지 데이터
    useEffect(() => {
        setMessages([
            { senderId: '868f8146000000ed6e44cef9', message: '현재로그인한 멤버 :: 안녕', regDt: new Date() },
            { senderId: '8690dc39000000ed6e44cefd', message: '다른그룹챗 멤버 :: 하이', regDt: new Date() },
            { senderId: '868f8146000000ed6e44cef9', message: '현재로그인한 멤버 :: 반가워', regDt: new Date() },
            { senderId: '8690dc39000000ed6e44cefd', message: '다른그룹챗 멤버 :: 하이22', regDt: new Date() },
        ]);
    }, []);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <div className="chat-container" style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div className="chat-header">
                <h2>채팅방: {roomId}</h2>
            </div>
            <div style={{ flex: 1, overflowY: 'auto', marginBottom: '10px' }}>
                {messages.map((message, index) => {
                    const member = members.find((m) => m.memberId === message.senderId);
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
                            {/* 1. 상대방 프로필 (왼쪽) */}
                            {!isCurrentUser && (
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                    marginRight: '10px'
                                }}>
                                    <img
                                        src={member?.profileImgUrl}
                                        alt="Profile"
                                        style={{
                                            width: '40px',
                                            height: '40px',
                                            borderRadius: '50%',
                                        }}
                                    />
                                    {/* member?.nickname 콘솔 출력 */}
                                    {console.log("Rendering nickname:", member?.nickname)}
                                    <strong style={{ padding: '5px' }}>{member?.nickname}</strong>
                                </div>
                            )}

                            {/* 2. 메시지 내용 */}
                            <div
                                style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: isCurrentUser ? 'flex-end' : 'flex-start',
                                }}
                            >
                                <div
                                    style={{
                                        backgroundColor: isCurrentUser ? '#DCF8C6' : 'white',
                                        padding: '10px',
                                        borderRadius: '10px',
                                        maxWidth: '60%',
                                    }}
                                >
                                    {message.message}
                                </div>
                                <div style={{ fontSize: '0.8em', color: '#aaa', padding: '5px' }}>
                                    {new Date(message.regDt).toLocaleTimeString()}
                                </div>
                            </div>

                            {/* 3. 내 프로필 (오른쪽) */}
                            {isCurrentUser && (
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                    marginLeft: '10px'
                                }}>
                                    <img
                                        src={member?.profileImgUrl}
                                        alt="Profile"
                                        style={{
                                            width: '40px',
                                            height: '40px',
                                            borderRadius: '50%',
                                        }}
                                    />
                                    {/*카카오톡처럼, 내 대화창에서는 닉네임 없음*/}
                                    {/*<strong style={{ padding: '5px' }}>{member?.nickname}</strong>*/}
                                </div>
                            )}
                        </div>
                    );
                })}
                <div ref={messagesEndRef} />
            </div>
        </div>
    );
}

export default ChatPageGroup;