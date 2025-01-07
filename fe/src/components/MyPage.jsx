import React, { useState, useEffect, useContext, useRef } from 'react';
import axiosInstance from '../axiosInstance';
import './MyPage.css';
import { FaUserCircle, FaCamera } from 'react-icons/fa';
import { IoSettingsOutline } from 'react-icons/io5';
import { Link, useNavigate } from 'react-router-dom';
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';
import { AppContext } from '../App';

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
    const navigate = useNavigate();
    const { updateLoginStatus } = useContext(AppContext);
    const fileInputRef = useRef(null);
    const [isHovering, setIsHovering] = useState(false);

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const handleDelete = () => {
        confirmAlert({
            title: '회원 탈퇴 확인',
            message: '정말로 탈퇴하시겠습니까?',
            buttons: [
                {
                    label: '예',
                    onClick: async () => {
                        try {
                            await axiosInstance.delete('/members/delete');
                            alert('회원 탈퇴가 완료되었습니다.');
                            sessionStorage.removeItem('accessToken');
                            updateLoginStatus(false);
                            navigate('/');
                        } catch (error) {
                            console.error('Error during member deletion:', error);
                            alert('회원 탈퇴 중 오류가 발생했습니다.');
                        }
                    },
                },
                {
                    label: '아니오',
                    onClick: () => {},
                },
            ],
        });
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

    const handleProfileImageClick = () => {
        fileInputRef.current.click();
    };

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        if (!file) {
            alert("파일을 선택해주세요.");
            return;
        }

        if (file.size === 0) {
            alert("빈 파일은 업로드할 수 없습니다.");
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axiosInstance.post('/images/profile', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setUserData({ ...userData, profileImgUrl: response.data.imageUrl });
            alert('프로필 사진이 성공적으로 변경되었습니다.');
        } catch (error) {
            console.error('Error uploading image:', error);
            alert('이미지 업로드 중 오류가 발생했습니다.');
        }
    };

    const handleMouseEnter = () => {
        setIsHovering(true);
    };

    const handleMouseLeave = () => {
        setIsHovering(false);
    };

    return (
        <div className="mypage-container">
            <input
                type="file"
                ref={fileInputRef}
                style={{ display: 'none' }}
                onChange={handleFileChange}
                accept="image/*"
            />
            <div
                className="profile-image"
                onClick={handleProfileImageClick}
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
                style={{ cursor: 'pointer' }}
            >
                {userData.profileImgUrl ? (
                    <img src={userData.profileImgUrl} alt="Profile" className="profile-img" />
                ) : (
                    <FaUserCircle className="default-profile-icon" />
                )}
                {isHovering && (
                    <div className="overlay">
                        <FaCamera className="camera-icon" />
                    </div>
                )}
            </div>
            <div className="mypage-header">
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
                            <button onClick={handleDelete} className="dropdown-item delete-button">
                                회원 탈퇴
                            </button>
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