import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance';
import './MemberProfilePage.css';
import { FaUserCircle } from 'react-icons/fa';
import { useParams } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function MemberProfilePage() {
    const [userData, setUserData] = useState({
        profileImgUrl: null,
        nickname: '',
        email: '',
        movieHeartCount: 0,
        movieCommentCount: 1,
        bookHeartCount: 0,
        bookCommentCount: 0
    });
    const [genreList, setGenreList] = useState([]);
    const { memberId } = useParams();

    useEffect(() => {
        const fetchMemberPageData = async () => {
            try {
                const response = await axiosInstance.get(`/members/${memberId}/profile`);
                setUserData(response.data);
            } catch (error) {
                console.error('Error fetching member page data:', error);
            }
        };

        const fetchGenreList = async () => {
            try {
                const response = await axiosInstance.get(`/members/${memberId}/genres`);
                console.log('Genre list response:', response.data);
                setGenreList(response.data);
            } catch (error) {
                console.error('Error fetching genre list:', error);
            }
        };

        fetchMemberPageData();
        fetchGenreList();
    }, [memberId]);

    return (
        <div className="memberpage-container">
            <ToastContainer />
            <div className="profile-image">
                {userData.profileImgUrl ? (
                    <img src={userData.profileImgUrl} alt="Profile" className="profile-img" />
                ) : (
                    <FaUserCircle className="default-profile-icon" />
                )}
            </div>
            <div className="memberpage-header">
                <div className="user-info">
                    <h2>{userData.nickname}</h2>
                    <p>{userData.email}</p>
                </div>
            </div>
            <div className="memberpage-stats">
                <div className="stat-item">
                    <span>{userData.movieHeartCount}</span>
                    <span>영화 찜</span>
                </div>
                <div className="stat-item">
                    <span>{userData.movieCommentCount}</span>
                    <span>영화 코멘트</span>
                </div>
                <div className="stat-item">
                    <span>{userData.bookHeartCount}</span>
                    <span>도서 찜</span>
                </div>
                <div className="stat-item">
                    <span>{userData.bookCommentCount}</span>
                    <span>도서 코멘트</span>
                </div>
            </div>
            <div className="memberpage-genre-list">
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

export default MemberProfilePage;