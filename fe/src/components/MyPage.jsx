import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance';
import './MyPage.css';
import { FaUserCircle } from 'react-icons/fa'; // 기본 프로필 이미지
import { IoSettingsOutline } from 'react-icons/io5'; // 설정 아이콘

function MyPage() {
    const [userData, setUserData] = useState({
        profileImgUrl: null,
        nickname: '',
        email: '',
        movieHeartCount: 0,
        movieCommentCount: 1,
    });

    useEffect(() => {
        const fetchMyPageData = async () => {
            try {
                const response = await axiosInstance.get('/members/myPage');
                setUserData(response.data);
            } catch (error) {
                console.error('Error fetching my page data:', error);
            }
        };

        fetchMyPageData();
    }, []);

    return (
        <div className="mypage-container">
            <div className="mypage-header">
                <div className="profile-image">
                    {userData.profileImgUrl ? (
                        <img src={userData.profileImgUrl} alt="Profile" />
                    ) : (
                        <FaUserCircle className="default-profile-icon" />
                    )}
                </div>
                <div className="user-info">
                    <h2>{userData.nickname}</h2>
                    <p>{userData.email}</p>
                </div>
                <div className="settings-icon">
                    <IoSettingsOutline className="settings-icon-comp" />
                </div>
            </div>
            <div className="mypage-stats">
                <div className="stat-item">
                    <span>{userData.movieHeartCount}</span>
                    <span>영화 찜</span>
                </div>
                <div className="stat-item">
                    <span>{userData.movieCommentCount}</span>
                    <span>영화 코멘트</span>
                </div>
            </div>
        </div>
    );
}

export default MyPage;