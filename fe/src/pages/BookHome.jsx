// import React, { useEffect, useState } from 'react';
// import axios from 'axios';
// import './BookHome.css';
// import {Link} from 'react-router-dom'; //  도서 디테일 페이지로 이동하기 위한 링크
// import { Link, useOutletContext } from 'react-router-dom'; // -- 로그인 상태 가져오기 위해 추가
// import BestsellerBooksComponent from "./BestsellerBooksComponent.jsx";
// import PopularBooksComponent from "./PopularBooksComponent.jsx";
// import NewBooksComponent from "./NewBooksComponent.jsx";
// import axiosInstance from "../axiosInstance.js"; // axioInstance 임포트
//
// function BookHome() {
//
//     const { isLoggedIn } = useOutletContext(); // Outlet context에서 isLoggedIn 가져오기
//     const [recommendedBooks, setRecommendedBooks] = useState([]);
//     const fetchRecommendedBooks = async () => {
//         try {
//             const response = await axiosInstance.get('/api/books/recommendations');
//             setRecommendedBooks(response.data);
//         } catch (error) {
//             console.error('Error fetching recommended books:', error);
//         }
//     };
//
//     useEffect(() => {
//         if (isLoggedIn) {
//             fetchRecommendedBooks();
//         }
//     }, [isLoggedIn]);
//
//     return (
//         <div className="book-home">
//             <BestsellerBooksComponent />
//             <PopularBooksComponent />
//             <NewBooksComponent />
//         </div>
//
//     );
// }


import React, { useCallback, useEffect, useState } from 'react';
import './BookHome.css';
import { Link, useOutletContext } from 'react-router-dom';

import BestsellerBooksComponent from "./BestsellerBooksComponent.jsx";
import PopularBooksComponent from "./PopularBooksComponent.jsx";
import NewBooksComponent from "./NewBooksComponent.jsx";
import axiosInstance from "../axiosInstance.js";

function BookHome() {
    const { isLoggedIn } = useOutletContext();
    const [recommendedBooks, setRecommendedBooks] = useState([]);
    const [dataLoaded, setDataLoaded] = useState(false); // 데이터 로딩 완료 여부 상태 추가

    const fetchRecommendedBooks = useCallback(async () => {
        try {
            console.log('fetch api 호출');
            const response = await axiosInstance.get('/books/recommendations');
            setRecommendedBooks(response.data);
            console.log('data : ', response.data);
        } catch (error) {
            console.error('Error fetching recommended books:', error);
        }
    }, []);

    useEffect(() => {
        console.log('useEffect (isLoggedIn):', isLoggedIn);
        if (isLoggedIn && !dataLoaded) { // dataLoaded 상태를 확인하여 한 번만 실행
            console.log('로그인 상태 확인, fetchRecommendedBooks 호출');
            fetchRecommendedBooks();
            setDataLoaded(true); // 데이터 로딩 완료 상태를 true로 설정
        }
    }, []); // 의존성 배열에 dataLoaded 추가
    return (
        <div className="book-home">
            <BestsellerBooksComponent />
            <PopularBooksComponent />
            <NewBooksComponent />

            {/* 로그인 상태이고, 추천 도서 목록이 있을 때만 렌더링 */}
            {isLoggedIn && recommendedBooks.length > 0 && (
                <div>
                    <h2>추천 도서</h2>
                    <div className="book-list">
                        {recommendedBooks.map((book) => (
                            <Link className="book-card" to={`/book/${book.bookId}`} key={book.bookId}>
                                <div>
                                    <img src={book.bookImgUrl} alt={book.title} className="book-image" />
                                    <div className="book-info">
                                        <h3 className="book-title">{book.title}</h3>
                                        {/*<p className="book-writer">*/}
                                        {/*    {book.crew.join(', ')} /!* 작가 정보 수정 *!/*/}
                                        {/*</p>*/}
                                        {/* 기타 도서 정보 */}
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}

export default BookHome;