import React, {useState, useEffect, useRef} from 'react';
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosInstance from '../axiosInstance'; // axios 인스턴스 import
import './ChatPage.css'; // CSS 파일 import

function ChatPage({ roomId, roomInfo }) {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(true); // 로그인 상태
    const [hasJoinedRoom, setHasJoinedRoom] = useState(false); // 채팅방 참여 이력
    const [message, setMessage] = useState(''); // 공지 메시지 표시
    const focusDivRef = useRef(null); // 포커스를 주기 위한 ref
    


    // roomId가 변경될 경우 새로 고침
    useEffect(() => {
        console.log('roomId:', roomId); // roomId의 값 확인
        if (roomId) {
            setHasJoinedRoom(false); // 새 방 입장 시 참여 상태 초기화 (추후 DB에서 참여 이력 조회)
        }
    }, [roomId]);

    // // 채팅방 입장 시 roomId 설정
    // useEffect(() => {
    //     const urlParams = new URLSearchParams(window.location.search);
    //     const roomIdFromParams = urlParams.get('roomId');
    //     if (roomIdFromParams) {
    //         setRoomId(roomIdFromParams);
    //     } else {
    //         // roomId가 없을 경우 기본값 또는 생성 로직 추가
    //         const generatedRoomId = 1; // 임시로 test로 설정.
    //         setRoomId(generatedRoomId);
    //     }
    // }, []);

    // WebSocket 연결 설정
    useEffect(() => {
        if (!roomId) return; // roomId가 없으면 연결하지 않음

        const client = new Client({
            webSocketFactory: () => new SockJS(`${process.env.VITE_BASE_URL_FOR_CONF}/ws-stomp`),
            connectHeaders: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
            },
            debug: (str) => {
                console.log('STOMP Debug:', str);
                console.log("메시지  : " + JSON.stringify(messages, null, 2));
            },
        });



        client.onConnect = () => {
            console.log('WebSocket Connected');
            // TODO: GroupChatPage에서는 group으로 처리
            client.subscribe(`/topic/chat/message/one-on-one${roomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });

            // 과거 메시지 로드
            axiosInstance
                .get(`/chat/history?roomId=${roomId}`)
                .then((response) => {
                    console.log("roomId : " + roomId);
                    setMessages(response.data);
                })
                .catch((error) => {
                    console.error('Error fetching chat history:', error);
                });
        };

        client.onStompError = (frame) => {
            console.error('STOMP Error:', frame);
        };

        client.activate();
        setStompClient(client);

        return () => {
            if (client.connected) {
                client.deactivate();
            }
        };
    }, [roomId]); // roomId가 변경될 때마다 재연결

    const sendMessage = () => {
        if (stompClient && newMessage) {
            const chatMessage = {
                roomId: roomId, // roomId 사용
                senderId: 'currentUserId', // 현재 사용자 ID (실제로는 인증 정보에서 가져와야 함)
                message: newMessage,
                regDt: new Date()
                // TODO: type 여기서 보내주기
            };

            stompClient.publish({
                destination: '/app/chat/message/one-on-one',
                body: JSON.stringify(chatMessage),
            });

            setNewMessage('');
        }
    };

    // Enter 키로 메시지 보내기
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            sendMessage();
        }
    };

    const messagesEndRef = useRef(null); // 스크롤을 위한 ref

    // 새 메시지가 추가될 때마다 스크롤을 맨 아래로 이동
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({behavior: 'smooth'});
    }, [messages]);

    // 메시지 입력창 선택 시 로그인 및 방 참여 상태 확인
    const handleInputFocus = () => {
        if (!isLoggedIn) {
            setMessage('로그인이 필요합니다.'); //로그인이 필요함을 메시지로 표시
            return;
        }

        if (!hasJoinedRoom) {
            const confirmJoin = window.confirm('해당 채팅방에 참여하시겠습니까?'); // 채팅방 참여를 묻는 메시지 창
            if (confirmJoin) {
                setHasJoinedRoom(true);
                localStorage.setItem(`hasJoinedRoom_${roomId}`, 'true'); // roomId 기반으로 참여 이력 저장
                setMessage(''); // 메시지 초기화
            } else {
                setMessage('채팅방에 참여하지 않았습니다.');
                focusDivRef.current.focus(); // 커서를 다른 요소로 이동 (더 이상 메시지 창이 나오지 않도록)
                return;
            }
        }
    };


    return (
        <div className="chat-container" style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div className="chat-header">
                <h2>채팅방: {roomInfo.roomName}</h2>
            </div>
            <div style={{ flex: 1, overflowY: 'auto', marginBottom: '10px' }}>
                {messages.map((message, index) => (
                    <div
                        key={index} style={{ marginBottom: '10px' }}
                        className={`message ${
                            message.senderId === 'currentUserId' ? 'own-message' : ''
                        }`}
                    >

                        <div ><strong>{message.senderId} : {message.message}</strong></div>
                        <div style={{ fontSize: '0.8em', color: '#aaa' }}>{message.regDt}</div>

                    </div>
                ))}
                {message && <div style={{ color: 'red' }}>{message}</div>} {/* 메시지 표시 */}
                <div ref={messagesEndRef}/>
                {/* 스크롤을 위한 빈 div */}
            </div>
            <div className="chat-input" style={{ display: 'flex', borderTop: '1px solid #ddd', padding: '10px' }}>
                <input
                    style={{ flex: 1, padding: '10px', border: '1px solid #ddd' }}
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyDown={handleKeyDown}
                    onFocus={handleInputFocus} // 입력 필드 포커스 시 체크 (로그인, 참여 상태 체크)
                    placeholder="메시지를 입력하세요"
                />
                <button onClick={sendMessage}
                        style={{ marginLeft: '10px', padding: '10px' }}
                        disabled={!isLoggedIn || !hasJoinedRoom}>보내기</button>
                <div ref={focusDivRef} tabIndex={-1} style={{ height: '0' }} /> {/* 포커스 이동용 div */}
            </div>
        </div>
    );
}

export default ChatPage;