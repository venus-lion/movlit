import React, { useState, useEffect, useContext } from 'react';
import axiosInstance from '../axiosInstance';
import './MyPage.css';
import { FaUserCircle } from 'react-icons/fa';
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