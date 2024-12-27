import React, { useState } from 'react';
import axios from 'axios';

const MemberRegisterSimple = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [password2, setPassword2] = useState('');
    const [nickname, setNickname] = useState('');
    const [dob, setDob] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (password !== password2) {
            alert('패스워드가 일치하지 않습니다.');
            return;
        }
        try {
            const response = await axios.post('/api/member/register', {
                email,
                password,
                nickname,
                dob,
            });
            console.log('Registration successful:', response.data);
            window.location.href = '/member/login';
        } catch (error) {
            console.error('Registration error:', error);
        }
    };

    return (
        <div style={{ margin: '50px' }}>
            <h1>사용자 등록</h1>
            <hr />
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="email"
                    placeholder="이메일"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <br />
                <br />
                <input
                    type="password"
                    name="password"
                    placeholder="패스워드"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br />
                <br />
                <input
                    type="password"
                    name="password2"
                    placeholder="패스워드 확인"
                    value={password2}
                    onChange={(e) => setPassword2(e.target.value)}
                />
                <br />
                <br />
                <input
                    type="text"
                    name="nickname"
                    placeholder="사용자 이름"
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                />
                <br />
                <br />
                <input
                    type="text"
                    name="dob"
                    placeholder="생년월일"
                    value={dob}
                    onChange={(e) => setDob(e.target.value)}
                />
                <br />
                <br />
                <button type="submit">가입</button>
            </form>
        </div>
    );
};

export default MemberRegisterSimple;