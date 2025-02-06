import React, {useContext, useEffect, useRef, useState} from 'react';
import axiosInstance from '../axiosInstance';
import './MyPage.css';
import {FaCamera, FaUserCircle} from 'react-icons/fa';
import {IoSettingsOutline} from 'react-icons/io5';
import {Link, useNavigate} from 'react-router-dom';
import {confirmAlert} from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';
import {AppContext} from '../App';
import {toast, ToastContainer} from 'react-toastify';

function MyPage() {
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
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const navigate = useNavigate();
    const {updateLoginStatus} = useContext(AppContext);
    const fileInputRef = useRef(null);
    const [isHovering, setIsHovering] = useState(false);

    // 팔로우, 팔로잉 관련 변수
    const [followerCount, setFollowerCount] = useState(0); // 팔로워 개수
    const [followingCount, setFollowingCount] = useState(0); // 팔로잉 개수
    const [memberId, setMemberId] = useState(null); // 멤버ID 상태변수 추가

    //현재 로그인한 사용자의 memberId 가져오기
    const fetchMemberId = async () => {
        try {
            const response = await axiosInstance.get('/members/id');
            console.log('로그인한 memberId 가져오기 !!! ');
            console.log(response.data.memberId);
            console.log(response.data.memberId.value);
            setMemberId(response.data.memberId);
        } catch (error) {
            console.error('Error fetching member ID:', error);
        }
    };

    // 팔로워 / 팔로잉 개수 가져오기
    const fetchFollowCounts = async (currentMemberId) => {
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

    // 클릭 이벤트 핸들러 추가
    const handleFollowerClick = () => {
        navigate('/my-followers');
    };

    const handleFollowingClick = () => {
        navigate('/my-followings');
    }

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
                            toast.success('회원 탈퇴가 완료되었습니다.');
                            sessionStorage.removeItem('accessToken');
                            updateLoginStatus(false);
                            navigate('/');
                        } catch (error) {
                            console.error('Error during member deletion:', error);
                            toast.error('회원 탈퇴 중 오류가 발생했습니다.');
                        }
                    },
                },
                {
                    label: '아니오',
                    onClick: () => {
                    },
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

        fetchMemberId(); // 로그인한 사용자 memberId 가져오기
        fetchMyPageData();
        fetchGenreList();
        //  fetchFollowCounts();
    }, []);

    useEffect(() => {
        if (memberId) {
            fetchFollowCounts(memberId); // memberId 설정된 후, 팔로워/팔로잉 개수 가져오기
        }

    }, [memberId]);

    const handleProfileImageClick = () => {
        fileInputRef.current.click();
    };

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        const MAX_FILE_SIZE = 2800 * 1024; // 2800KB in bytes

        if (!file) {
            toast.warn('이미지를 선택해주세요.');
            return;
        }

        if (file.size === 0) {
            toast.error('빈 파일은 업로드할 수 없습니다.');
            return;
        }

        if (file.size > MAX_FILE_SIZE) {
            toast.error('이미지 크기가 2800KB를 초과합니다.');
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
            setUserData({...userData, profileImgUrl: response.data.imageUrl});
            toast.success('프로필 사진이 성공적으로 변경되었습니다.\n새로고침을 해주세요.');
        } catch (error) {
            if (error.response && error.response.status === 413) {
                toast.error('이미지 크기가 너무 큽니다. 2800KB 이하의 이미지를 업로드해주세요.');
            } else {
                toast.error('이미지 업로드 중 오류가 발생했습니다.');
            }
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
                style={{display: 'none'}}
                onChange={handleFileChange}
                accept="image/*"
            />
            <div
                className="profile-image"
                onClick={handleProfileImageClick}
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
                style={{cursor: 'pointer'}}
            >
                {userData.profileImgUrl ? (
                    <img src={userData.profileImgUrl} alt="Profile" className="profile-img"/>
                ) : (
                    <FaUserCircle className="default-profile-icon"/>
                )}
                {isHovering && (
                    <div className="overlay">
                        <FaCamera className="camera-icon"/>
                    </div>
                )}
            </div>
            <div className="mypage-header">
                <div className="user-info">
                    <h2>{userData.nickname}</h2>
                    <p>{userData.email}</p>
                </div>
                <div className="settings-icon" onClick={toggleDropdown}>
                    <IoSettingsOutline className="settings-icon-comp"/>
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
                    <span onClick={handleFollowerClick} className="link-button">{followerCount}</span>
                    <span>팔로워</span>
                </div>
                <div className="stat-item">
                    <span onClick={handleFollowingClick} className="link-button">{followingCount}</span>
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