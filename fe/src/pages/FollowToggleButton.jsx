// FollowToggleButton.jsx (새 파일 생성)
import React, {useEffect, useState} from 'react';
import axiosInstance from '../axiosInstance';
import {toast} from 'react-toastify';

function FollowToggleButton({memberId}) {
    const [isFollowing, setIsFollowing] = useState(false);

    const checkFollowStatus = async () => {
        try {
            const response = await axiosInstance.get(`/follows/check/${memberId}`);
            setIsFollowing(response.data.following);
        } catch (error) {
            console.error('Error checking follow status:', error);
        }
    };

    useEffect(() => {
        checkFollowStatus();
    }, [memberId]);

    const handleFollowToggle = async () => {
        try {
            if (isFollowing) {
                await axiosInstance.delete(`/follows/${memberId}/follow`);
                setIsFollowing(false);
            } else {
                await axiosInstance.post(`/follows/${memberId}/follow`);
                setIsFollowing(true);
            }
            toast.success(isFollowing ? '언팔로우 하였습니다.' : '팔로우 하였습니다.');
        } catch (error) {
            console.error('Error toggling follow status:', error);
            toast.error('팔로우/언팔로우 처리에 실패했습니다.');
        }
    };

    return (
        <button onClick={handleFollowToggle} className="follow-button">
            {isFollowing ? '언팔로우' : '팔로우'}
        </button>
    );
}

export default FollowToggleButton;