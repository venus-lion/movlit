import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance';
import './FollowList.css';
import { FaUserCircle } from 'react-icons/fa';
import FollowToggleButton from "./FollowToggleButton.jsx";

function FollowList({type}){
    const [followList, setFollowList] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                let response;
                if (type === 'followers'){ // 내 팔로워
                    response = await axiosInstance.get(`/follows/my/follow/details`);
                } else { // 내 팔로잉
                    response = await axiosInstance.get(`/follows/my/following/details`);
                }
                console.log('팔로잉 / 팔로우 fetch DATA ');
                console.log(response.data);
                setFollowList(response.data);
            } catch (error) {
                console.error('Error fetching follow list:', error);
            }
        };

        fetchData();

    }, [type]);

    return (
        <div className="follow-list-container">
            <h2>{type === 'followers' ? '나를 팔로우하는 사람들' : '내가 팔로우하는 사람들'}</h2>
            <div className="follow-list">
                {followList.map((follow) => (
                    <div key={follow.memberId} className="follow-item">
                        <div className="profile-image">
                            {follow.profileImgUrl ? (
                                <img src={follow.profileImgUrl} alt="Profile" className="profile-img" />
                            ) : (
                                <FaUserCircle className="default-profile-icon" />
                            )}
                        </div>
                        <div className="user-info">
                            <div className="nickname">{follow.nickname}</div>
                            <div className="email">{follow.email}</div>
                        </div>
                        {/* 팔로잉/팔로우 버튼 (MemberProfilePage에서 가져옴) */}
                        <FollowToggleButton memberId={follow.memberId} />
                    </div>
                ))}
            </div>
        </div>
    );

}

export default FollowList;