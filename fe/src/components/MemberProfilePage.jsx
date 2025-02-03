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

    // 팔로잉, 팔로우 관련 변수 추가
    const [isFollowing, setIsFollowing] = useState(false); // 팔로우 상태
    const [followerCount, setFollowerCount] = useState(0); // 팔로워 개수
    const [followingCount, setFollowingCount] = useState(0); // 팔로잉 개수

    // 팔로우 상태 확인
    const checkFollowStatus = async () => {
      try {
          const response = await axiosInstance.get(`/follows/check/${memberId}`);
          console.log('프론트에서 팔로우 팔로잉 여부 api 호출 결과 !!');
          console.log(response.data.following);

          setIsFollowing(response.data.following); // 서버로부터 팔로우 상태를 받아옴
      } catch (error){
          console.error('Error checking follow status : ', error);
      }
    };

    // 팔로워 / 팔로잉 개수 가져오기
    const fetchFollowCounts = async () => {
        const followerCountResponse =
            await axiosInstance.get(`/follows/${memberId}/followers/count`);
        console.log('프론트, 나를 팔로우하는, 팔로워 개수 가져오기 !! ');
        console.log(followerCountResponse.data);
        setFollowerCount(followerCountResponse.data);

        const followeeCountResponse =
            await axiosInstance.get(`/follows/${memberId}/follows/count`);
        console.log('프론트, 내가 팔로우하는, 팔로우 개수 가져오기 !! ');
        console.log(followeeCountResponse.data);

        setFollowingCount(followeeCountResponse.data);

    };

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

        checkFollowStatus();
        fetchFollowCounts();
    }, [memberId]);

    // 팔로우, 언팔로우 기능 처리
    const handleFollowToggle = async () => {
        try {
            // 'test'가 'elsa'를 팔로우한 상태
            if (isFollowing) {
                // 언팔로우 (팔로잉 -> 팔로우) -- 'test'가 'elsa'를 언랄로우 / memberId : elsa
                await axiosInstance.delete(`/follows/${memberId}/follow`);
                setIsFollowing(false);
                setFollowerCount(prevCount => prevCount - 1);

            } else {
                // 팔로우 (팔로우 -> 팔로잉)
                await axiosInstance.post(`/follows/${memberId}/follow`);
                setIsFollowing(true);
                setFollowerCount(prevCount => prevCount + 1);
            }

            toast.success(isFollowing ? '언팔로우 하였습니다.' : '팔로우 하셨습니다.');

            // 팔로우 상태 및 카운트 업데이트 이후, 추가 작업 수행 (상태 다시 불러오기)
            checkFollowStatus();
            fetchFollowCounts();
        } catch (error) {
            console.error('Error toggling follow status:', error);
            toast.error('팔로우/언팔로우 처리에 실패했습니다.');
        }
    };

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
                    <button onClick={handleFollowToggle} className="follow-button">
                        {isFollowing ? '언팔로우' : '팔로우'}
                    </button>
                </div>
            </div>
            <div className="memberpage-stats">
                <div className="stat-item">
                    <span>{followerCount}</span>
                    <span>팔로워</span>
                </div>
                <div className="stat-item">
                    <span>{followingCount}</span>
                    <span>팔로잉</span>
                </div>
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