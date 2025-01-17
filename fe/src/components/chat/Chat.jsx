import React, {useState} from 'react';
import ChatTabs from './ChatTabs';
import ChatList from './ChatList';
import ChatWindow from './ChatWindow';

const Chat = () => {
    const [activeTab, setActiveTab] = useState('personal'); // 개인 채팅 또는 그룹 채팅
    const [searchTerm, setSearchTerm] = useState(''); // 검색어
    const [selectedChat, setSelectedChat] = useState(null); // 현재 선택된 채팅방

    // 검색 핸들러
    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    return (
        <div style={{display: 'flex', height: 'calc(100vh - 60px)'}}>
            {/* 왼쪽: 채팅 목록 */}
            <div style={{width: '30%', borderRight: '1px solid #ddd', padding: '10px'}}>
                <ChatTabs activeTab={activeTab} setActiveTab={setActiveTab}/>
                <input
                    type="text"
                    placeholder={`${
                        activeTab === 'personal' ? '개인 채팅 검색' : '그룹 채팅 검색'
                    }`}
                    value={searchTerm}
                    onChange={handleSearch}
                    style={{
                        width: '100%',
                        padding: '10px',
                        marginBottom: '10px',
                        border: '1px solid #ddd',
                        borderRadius: '4px',
                    }}
                />
                <ChatList
                    activeTab={activeTab}
                    searchTerm={searchTerm}
                    onSelectChat={(chat) => setSelectedChat(chat)}
                />
            </div>

            {/* 오른쪽: 채팅 화면 */}
            <div style={{flex: 1, padding: '10px'}}>
                {selectedChat ? (
                    <ChatWindow chat={selectedChat}/>
                ) : (
                    <div style={{textAlign: 'center', marginTop: '20%'}}>
                        채팅방을 선택해주세요.
                    </div>
                )}
            </div>
        </div>
    );
};

export default Chat;
