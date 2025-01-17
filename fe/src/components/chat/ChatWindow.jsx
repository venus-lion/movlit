import React, {useEffect, useRef, useState} from 'react';

const ChatWindow = ({ roomId }) => {
    const [messages, setMessages] = useState([
        {id: 1, sender: '김현경', text: '안녕하세요!', time: '오전 9:46'},
        {id: 2, sender: '나', text: '안녕하세요! 반갑습니다.', time: '오전 9:50'},
    ]);
    const [newMessage, setNewMessage] = useState('');
    //const { isLoggedIn } = useOutletContext();// 로그인 상태
    const [isLoggedIn, setIsLoggedIn] = useState(true); // 채팅방 참여 이력
    const [hasJoinedRoom, setHasJoinedRoom] = useState(false); // 채팅방 참여 이력
    const [message, setMessage] = useState(''); // 공지 메시지 표시
    const focusDivRef = useRef(null); // 포커스를 주기 위한 ref


    // 컴포넌트가 마운트될 때 로컬 스토리지에서 참여 이력 확인
    // (추후 DB에서 가져오는 것으로 수정)
    useEffect(() => {
        console.log(">> 방 ID : " + roomId);
        const joined = localStorage.getItem(`hasJoinedRoom_${roomId}`); // roomId에 맞는 참여 이력 체크
        if (joined === 'true') {
            setHasJoinedRoom(true);
        }
    }, [roomId]);



    const handleSend = () => {
        if (!newMessage.trim()) return;

        setMessages([
            ...messages,
            {id: Date.now(), sender: '나', text: newMessage, time: '지금'},
        ]);
        setNewMessage('');
    };

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

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault(); // 기본 동작 방지 (예: 줄바꿈)
            handleSend(); // 메시지 전송
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div style={{ flex: 1, overflowY: 'auto', marginBottom: '10px' }}>
                {messages.map((msg) => (
                    <div key={msg.id} style={{ marginBottom: '10px' }}>
                        <strong>{msg.sender}</strong>: {msg.text}
                        <div style={{ fontSize: '0.8em', color: '#aaa' }}>{msg.time}</div>
                    </div>
                ))}
                {message && <div style={{ color: 'red' }}>{message}</div>} {/* 메시지 표시 */}
            </div>
            <div style={{ display: 'flex', borderTop: '1px solid #ddd', padding: '10px' }}>
                <input
                    style={{ flex: 1, padding: '10px', border: '1px solid #ddd' }}
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="메시지를 입력하세요"
                    onFocus={handleInputFocus} // 입력 필드 포커스 시 체크 (로그인, 참여 상태 체크)
                    onKeyDown={handleKeyDown} // Enter 키 눌렀을 때 전송
                />
                <button
                    onClick={handleSend}
                    style={{ marginLeft: '10px', padding: '10px' }}
                    disabled={!isLoggedIn || !hasJoinedRoom}
                >
                    전송
                </button>
                <div ref={focusDivRef} tabIndex={-1} style={{ height: '0' }} /> {/* 포커스 이동용 div */}
            </div>
        </div>
    );
};

export default ChatWindow;
