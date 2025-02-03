import React, {useEffect, useState} from 'react';
import './Home.css';
import '../App.css';
import { Link, useOutletContext } from 'react-router-dom';
import axiosInstance from "../axiosInstance.js";




function Notification() {
    const [myNotifications, setMyNotifications] = useState([]);



    useEffect(() => {
        // 이곳에서 알림을 가져오는 API를 호출하세요


        const fetchedMyNotifications = async () => {
            try {
                const response = await axiosInstance.get('/notification');

                setMyNotifications(response.data);
            } catch (err) {
                console.error(`Error fetching myNotifications : `, err);
            }
        }

        fetchedMyNotifications();
    }, []);

    // 선택한 알림 삭제
    const deleteNotification = async (notificationId) => {
        try {

            console.log('최종 삭제할 id:', notificationId); // 최종적으로 삭제할 ID 확인
            await axiosInstance.delete(`/notification/${notificationId}`);

            // API 호출이 성공한 후 상태 업데이트: 삭제한 알림을 상태에서 제거
            setMyNotifications(prevMyNotifications =>
                prevMyNotifications.filter(notification => notification.id !== notificationId)
            );


        } catch (err) {
            console.error('Error while deleting notification:', err);
        }
    };

    // 모든 알림 삭제
    const deleteAllmyNotifications = async () => {
        try{
            await axiosInstance.delete(`/notification/all`);
            setMyNotifications([]);

        }catch (err){
            console.error('Error while deleting notification:', err);
        }


    };

    return (
        <div className="notification-container">
            <h1>
                <img src="/images/notification-bell-icon.png" alt="아이콘" className="noti-icon" />
                나의 알림
                <button className="delete-all-btn" onClick={deleteAllmyNotifications}>모두 삭제</button>
            </h1>
            <div className="notification-list">
                {myNotifications.map((notification) => {
                    // 각 notification.type에 따라 이미지 경로를 선택
                    let imgSrc;
                    switch (notification.type) {
                        case 'CONTENT_HEART_CHATROOM':
                            imgSrc = '/images/notification-heartmark-icon.png'; // 하트 아이콘
                            break;
                        case 'FOLLOW':
                            imgSrc = '/images/notification-follow-icon.png'; // 팔로우 아이콘
                            break;
                        case 'ONE_ON_ONE_CHAT':
                            imgSrc = '/images/notification-onechat-icon.png'; // 1대1 채팅 아이콘
                            break;
                        case 'GROUP_CHAT':
                            imgSrc = '/images/notification-groupchat-icon.png'; // 그룹 채팅 아이콘
                            break;
                        default:
                            imgSrc = '/images/notification-bell-icon.png'; // 기본 아이콘
                    }

                    return (
                        <div key={notification.id} className="notification-item">
                            <img src={imgSrc} alt="아이콘" className="noti-icon" />
                            <p className="noti-message">{notification.message}</p>
                            <button className="delete-btn" onClick={() => deleteNotification(notification.id)}>X</button>
                        </div>
                    );
                })}
            </div>
        </div>
    );






};

export default Notification;
