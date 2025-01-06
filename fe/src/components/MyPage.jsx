import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance';
import './MyPage.css';
import { FaUserCircle } from 'react-icons/fa';
import { IoSettingsOutline } from 'react-icons/io5';
import { Link } from 'react-router-dom';

function MyPage() {
    const [userData, setUserData] = useState({
        profileImgUrl: null,
        nickname: '',
        email: '',
        movieHeartCount: 0,
        movieCommentCount: 1,
    });
    const [genreList, setGenreList] = useState([]);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    useEffect(() => {
        const fetchMyPageData = async () => {
            try {
                const response = await axiosInstance.get('/members/myPage');
                setUserData(response.data);
            } catch (error) {
                console.error('Error fetching my page data:', error);
            }
        };

        const fetchGenreList = async () => {
            try {
                const response = await axiosInstance.get('/members/genreList');
                console.log('Genre list response:', response.data);
                setGenreList(response.data);
            } catch (error) {
                console.error('Error fetching genre list:', error);
            }
        };

        fetchMyPageData();
        fetchGenreList();
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
                <div className="settings-icon" onClick={toggleDropdown}>
                    <IoSettingsOutline className="settings-icon-comp" />
                    {isDropdownOpen && (
                        <div className="dropdown-menu">
                            <Link to="/member/update" className="dropdown-item">
                                회원 수정
                            </Link>
                            {/* 회원 탈퇴는 링크 대신 보여주기만 합니다. */}
                            <span className="dropdown-item disabled">회원 탈퇴</span>
                        </div>
                    )}
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
            <div className="mypage-genre-list">
                <h3>선호 장르</h3>
                <div className="genre-chips">
                    {genreList.map((genre) => (
                        <div key={genre.genreId} className="genre-chip">
                            {genre.genreName}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default MyPage;