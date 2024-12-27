import React, { useState, useEffect } from 'react';
import axios from 'axios';

const MemberList = () => {
    const [members, setMembers] = useState([]);

    useEffect(() => {
        const fetchMembers = async () => {
            try {
                const response = await axios.get('/api/members'); // 백엔드 API 엔드포인트
                setMembers(response.data);
            } catch (error) {
                console.error('Error fetching members:', error);
            }
        };

        fetchMembers();
    }, []);

    return (
        <div>
            {/* ... 상단 및 사이드바 ... */}
            <div className="container" style={{ marginTop: '80px' }}>
                <div className="row">
                    <div className="col-3">
                        {/* ... 사이드바 ... */}
                    </div>
                    <div className="col-9">
                        <main>
                            <h3>
                                <strong>사용자 목록</strong>
                            </h3>
                            <hr />
                            <table className="table">
                                <thead>
                                <tr>
                                    <th style={{ width: '8%' }}>사진</th>
                                    <th style={{ width: '20%' }}>memberId</th>
                                    <th style={{ width: '14%' }}>이름</th>
                                    <th style={{ width: '18%' }}>이메일</th>
                                    <th style={{ width: '10%' }}>등록일</th>
                                    <th style={{ width: '8%' }}>권한</th>
                                    <th style={{ width: '10%' }}>Provider</th>
                                    <th style={{ width: '10%' }}>액션</th>
                                </tr>
                                </thead>
                                <tbody>
                                {members.map((member) => (
                                    <tr key={member.memberId}>
                                        <td>
                                            <img
                                                src={member.profileImgUrl}
                                                alt="picture"
                                                height="48"
                                                style={{ borderRadius: '50%' }}
                                            />
                                        </td>
                                        <td>{member.memberId}</td>
                                        <td>{member.nickname}</td>
                                        <td>{member.email}</td>
                                        <td>{member.regDt}</td>
                                        <td>{member.role.substring(5)}</td>
                                        <td>{member.provider}</td>
                                        <td>
                                            <a href={`/member/update/${member.memberId}`}>
                                                <i className="fa-solid fa-pencil"></i>
                                            </a>
                                            <a href={`/member/delete/${member.memberId}`}>
                                                <i className="fa-solid fa-user-minus"></i>
                                            </a>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </main>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MemberList;