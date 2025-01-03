import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './BookHome.css';
import {Link} from 'react-router-dom'; //  도서 디테일 페이지로 이동하기 위한 링크

function BookHome() {
    // 베스트셀러 상태
    const [bestsellers, setBestsellers] = useState([]);

    // 신작도서 상태
    const [newBooks, setNewBooks] = useState([]);

    // 인기도서 상태
    const [popularBooks, setPopularBooks] = useState([]);

    // 각 도서들 상태 설정
    const [startIndexBestsellers, setStartIndexBestsellers] = useState(0);
    const [startIndexNewBooks, setStartIndexNewBooks] = useState(0);
    const [startIndexPopularBooks, setStartIndexPopularBooks] = useState(0);

    // 베스트셀러 데이터 가져오기
    useEffect(() => {
        const fetchBestsellers = async () => {
            try {
                const response = await axios.get('/api/books/bestseller', {
                    params: { limit: 30 },
                });
                setBestsellers(response.data.books);
            } catch (error) {
                console.error('Error fetching bestsellers:', error);
            }
        };

        fetchBestsellers();
    }, []);

    // 신간 데이터 가져오기
    useEffect(() => {
        const fetchNewBooks = async () => {
            try {
                const response = await axios.get('/api/books/new', {
                    params: { limit: 30 },
                });
                setNewBooks(response.data.books);
            } catch (error) {
                console.error('Error fetching new books:', error);
            }
        };

        fetchNewBooks();
    }, []);

    // 인기 도서 데이터 가져오기
    useEffect(() => {
        const fetchPopularBooks = async () => {
            try {
                const response = await axios.get('/api/books/popular', {
                    params: { limit: 30 },
                });
                setPopularBooks(response.data.books);
            } catch (error) {
                console.error('Error fetching popular books:', error);
            }
        };

        fetchPopularBooks();
    }, []);

    const handleNextBestsellers = () => {
        if (startIndexBestsellers + 5 < bestsellers.length) {
            setStartIndexBestsellers(startIndexBestsellers + 5);
        }
    };

    const handlePrevBestsellers = () => {
        if (startIndexBestsellers > 0) {
            setStartIndexBestsellers(startIndexBestsellers - 5);
        }
    };

    const handleNextNewBooks = () => {
        if (startIndexNewBooks + 5 < newBooks.length) {
            setStartIndexNewBooks(startIndexNewBooks + 5);
        }
    };

    const handlePrevNewBooks = () => {
        if (startIndexNewBooks > 0) {
            setStartIndexNewBooks(startIndexNewBooks - 5);
        }
    };

    const handleNextPopularBooks = () => {
        if (startIndexPopularBooks + 5 < popularBooks.length) {
            setStartIndexPopularBooks(startIndexPopularBooks + 5);
        }
    };

    const handlePrevPopularBooks = () => {
        if (startIndexPopularBooks > 0) {
            setStartIndexPopularBooks(startIndexPopularBooks - 5);
        }
    };

    return (
        <div className="book-home">
            <h2>신간 베스트셀러 순위</h2>
            <div className="book-carousel">
                {startIndexBestsellers > 0 && (
                    <button className="prev-button" onClick={handlePrevBestsellers}>
                        {'<'}
                    </button>
                )}
                <div className="book-list">
                    {bestsellers
                        .slice(startIndexBestsellers, startIndexBestsellers + 5)
                        .map((book, index) => (
                            <Link className="book-card" to={`/book/${book.bookId}`} key={book.bookId}>
                                <div key={book.bookId}>
                                    <div className="book-rank">
                                        {startIndexBestsellers + index + 1}
                                    </div>
                                    <img
                                        src={book.bookImgUrl}
                                        alt={book.title}
                                        className="book-image"
                                    />
                                    <div className="book-info">
                                        <h3 className="book-title">{book.title}</h3>
                                        <p className="book-writer">
                                            {book.writers.map((writer) => writer.name).join(', ')}
                                        </p>
                                    </div>
                                </div>
                            </Link>
                        ))}
                </div>
                {startIndexBestsellers + 5 < bestsellers.length && (
                    <button className="next-button" onClick={handleNextBestsellers}>
                        {'>'}
                    </button>
                )}
            </div>

            <h2>책 신간 순위</h2>
            <div className="book-carousel">
                {startIndexNewBooks > 0 && (
                    <button className="prev-button" onClick={handlePrevNewBooks}>
                        {'<'}
                    </button>
                )}
                <div className="book-list">
                    {newBooks
                        .slice(startIndexNewBooks, startIndexNewBooks + 5)
                        .map((book, index) => (
                            <Link className="book-card" to={`/book/${book.bookId}`} key={book.bookId}>
                                <div key={book.bookId}>
                                    <div className="book-rank">
                                        {startIndexNewBooks + index + 1}
                                    </div>
                                    <img
                                        src={book.bookImgUrl}
                                        alt={book.title}
                                        className="book-image"
                                    />
                                    <div className="book-info">
                                        <h3 className="book-title">{book.title}</h3>
                                        <p className="book-writer">
                                            {book.writers.map((writer) => writer.name).join(', ')}
                                        </p>
                                    </div>
                                </div>
                            </Link>
                        ))}
                </div>
                {startIndexNewBooks + 5 < newBooks.length && (
                    <button className="next-button" onClick={handleNextNewBooks}>
                        {'>'}
                    </button>
                )}
            </div>

            <h2>인기 도서 순위</h2> {/* 추가 */}
            <div className="book-carousel">
                {startIndexPopularBooks > 0 && (
                    <button className="prev-button" onClick={handlePrevPopularBooks}>
                        {'<'}
                    </button>
                )}
                <div className="book-list">
                    {popularBooks
                        .slice(startIndexPopularBooks, startIndexPopularBooks + 5)
                        .map((book, index) => (
                            <Link className="book-card" to={`/book/${book.bookId}`} key={book.bookId}>
                                <div key={book.bookId}>
                                    <div className="book-rank">
                                        {startIndexPopularBooks + index + 1}
                                    </div>
                                    <img
                                        src={book.bookImgUrl}
                                        alt={book.title}
                                        className="book-image"
                                    />
                                    <div className="book-info">
                                        <h3 className="book-title">{book.title}</h3>
                                        <p className="book-writer">
                                            {book.writers.map((writer) => writer.name).join(', ')}
                                        </p>
                                    </div>
                                </div>
                            </Link>
                        ))}
                </div>
                {startIndexPopularBooks + 5 < popularBooks.length && (
                    <button className="next-button" onClick={handleNextPopularBooks}>
                        {'>'}
                    </button>
                )}
            </div>
        </div>
    );
}

export default BookHome;