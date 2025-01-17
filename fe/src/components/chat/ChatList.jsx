import React from 'react';

const ChatList = ({activeTab, searchTerm, onSelectChat}) => {
    const personalChats = [
        {id: 1, name: '김현경', lastMessage: '안녕하세요!', time: '8시간 전'},
        {id: 2, name: '박상원', lastMessage: '회의 일정입니다.', time: '2시간 전'},
    ];

    const groupChats = [
        {id: 1, name: '콘텐츠기획팀', lastMessage: '점심 같이 먹어요.', time: '4시간 전'},
        {id: 2, name: '마케팅팀', lastMessage: '프로젝트 진행 상황 보고.', time: '1시간 전'},
    ];

    const chats = activeTab === 'personal' ? personalChats : groupChats;

    // 검색어 필터링
    const filteredChats = chats.filter((chat) =>
        chat.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div>
            {filteredChats.map((chat) => (
                <div
                    key={chat.id}
                    style={{
                        padding: '10px',
                        borderBottom: '1px solid #ddd',
                        cursor: 'pointer',
                    }}
                    onClick={() => onSelectChat(chat)}
                >
                    <div style={{fontWeight: 'bold'}}>{chat.name}</div>
                    <div style={{fontSize: '0.9em', color: '#666'}}>{chat.lastMessage}</div>
                    <div style={{fontSize: '0.8em', color: '#aaa'}}>{chat.time}</div>
                </div>
            ))}
        </div>
    );
};

export default ChatList;
