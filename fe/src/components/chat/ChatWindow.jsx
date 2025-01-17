import React, {useState} from 'react';

const ChatWindow = ({chat}) => {
    const [messages, setMessages] = useState([
        {id: 1, sender: '김현경', text: '안녕하세요!', time: '오전 9:46'},
        {id: 2, sender: '나', text: '안녕하세요! 반갑습니다.', time: '오전 9:50'},
    ]);
    const [newMessage, setNewMessage] = useState('');

    const handleSend = () => {
        if (!newMessage.trim()) return;

        setMessages([
            ...messages,
            {id: Date.now(), sender: '나', text: newMessage, time: '지금'},
        ]);
        setNewMessage('');
    };

    return (
        <div style={{display: 'flex', flexDirection: 'column', height: '100%'}}>
            <div style={{flex: 1, overflowY: 'auto', marginBottom: '10px'}}>
                {messages.map((msg) => (
                    <div key={msg.id} style={{marginBottom: '10px'}}>
                        <strong>{msg.sender}</strong>: {msg.text}
                        <div style={{fontSize: '0.8em', color: '#aaa'}}>{msg.time}</div>
                    </div>
                ))}
            </div>
            <div style={{display: 'flex', borderTop: '1px solid #ddd', padding: '10px'}}>
                <input
                    style={{flex: 1, padding: '10px', border: '1px solid #ddd'}}
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="메시지를 입력하세요"
                />
                <button onClick={handleSend} style={{marginLeft: '10px', padding: '10px'}}>
                    전송
                </button>
            </div>
        </div>
    );
};

export default ChatWindow;
