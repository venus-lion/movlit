import React, { useState } from 'react';
import './Home.css';
import { useOutletContext } from 'react-router-dom';

import BestsellerBooksComponent from "./BestsellerBooksComponent.jsx";
import PopularBooksComponent from "./PopularBooksComponent.jsx";
import NewBooksComponent from "./NewBooksComponent.jsx";
import BookCarouselRecommend from "./BookCarouselRecommend.jsx";
import useApiData from "../hooks/userRecommendBookApi.jsx";
import RandomGenreBooksComponent from "./RandomGenreBooksComponent.jsx";

function BookHome() {
    const { isLoggedIn } = useOutletContext();

    // 사용자 찜한 도서 기반 추천 도서 API 호출
    const {
        data: recommendedBooks,
        loading: loadingRecommended,
        error: errorRecommended
    } = useApiData('/books/recommendations', isLoggedIn);

    // 사용자 관심 장르 도서 API 호출
    const {
        data: interestGenreBooks,
        loading: loadingInterestGenre,
        error: errorInterestGenre
    } = useApiData('/books/interestGenre', isLoggedIn);

    const [startIndexRecommended, setStartIndexRecommended] = useState(0);
    const [startIndexInterestGenre, setStartIndexInterestGenre] = useState(0);

    const handleNext = (startIndex, setStartIndex, length) => {
        const newIndex = startIndex + 5;
        if (newIndex < length) {
            setStartIndex(newIndex);
        }
    };

    const handlePrev = (startIndex, setStartIndex) => {
        const newIndex = startIndex - 5;
        if (newIndex >= 0) {
            setStartIndex(newIndex);
        }
    };

    const handleNextRecommended = () => handleNext(startIndexRecommended, setStartIndexRecommended, recommendedBooks.length);
    const handlePrevRecommended = () => handlePrev(startIndexRecommended, setStartIndexRecommended);
    const handleNextInterestGenre = () => handleNext(startIndexInterestGenre, setStartIndexInterestGenre, interestGenreBooks.length);
    const handlePrevInterestGenre = () => handlePrev(startIndexInterestGenre, setStartIndexInterestGenre);



    return (
        <div className="book-home">
            <BestsellerBooksComponent />
            <PopularBooksComponent />
            <NewBooksComponent />
            <RandomGenreBooksComponent />

            {isLoggedIn && interestGenreBooks.length > 0 && (
                <BookCarouselRecommend
                    title="회원님의 취향저격 도서 장르"
                    books={interestGenreBooks}
                    startIndex={startIndexInterestGenre}
                    handlePrev={handlePrevInterestGenre}
                    handleNext={handleNextInterestGenre}
                />
            )}

            {isLoggedIn && recommendedBooks.length > 0 && (
                <BookCarouselRecommend
                    title="회원님이 찜한 책과 닮은 도서들"
                    books={recommendedBooks}
                    startIndex={startIndexRecommended}
                    handlePrev={handlePrevRecommended}
                    handleNext={handleNextRecommended}
                />
            )}

            {loadingRecommended && <p>Loading recommended books...</p>}
            {errorRecommended && <div>Error loading recommended books.</div>}
            {loadingInterestGenre && <p>Loading interest genre books...</p>}
            {errorInterestGenre && <div>Error loading interest genre books.</div>}
        </div>
    );
}

export default BookHome;