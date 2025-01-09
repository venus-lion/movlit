import React, {useEffect, useState} from "react";
import {FaStar, FaRegStar, FaStarHalfAlt} from 'react-icons/fa';
import {Link, useParams} from 'react-router-dom';
import './SearchPage.css';
import './Home.css'
import axios from "axios";
import axiosInstance from "../axiosInstance.js";

function SearchPage() {
    // 별을 표시하는 함수
    const renderStars = (rating) => {
        // rating 값을 0 ~ 10으로 받을 경우
        const validRating = Math.max(0, Math.min(10, rating || 0));  // 0 ~ 10 사이로 제한

        // 2점마다 1개의 꽉 찬 별로 환산
        const fullStars = Math.floor(validRating / 2);  // 꽉 찬 별 개수
        const halfStar = validRating % 2 >= 1 ? 1 : 0;  // 반쪽 별 여부 (나머지가 1 이상이면 반쪽 별)
        const emptyStars = 5 - fullStars - halfStar;  // 빈 별 개수 (총 5개 별이므로 나머지)

        return (
            <>
                {[...Array(fullStars)].map((_, index) => <FaStar key={`full-${index}`} className="star-icon"/>)}
                {halfStar === 1 && <FaStarHalfAlt className="star-icon"/>}
                {[...Array(emptyStars)].map((_, index) => <FaRegStar key={`empty-${index}`} className="star-icon"/>)}
            </>
        );
    };

    const {inputStr} = useParams();
    // const inputStr = searchParams.get('inputStr'); // 쿼리 파라미터에서 검색어 가져오기
    const [movieList, setMovies] = useState([]);
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        console.log('useEffect 실행됨, query:', inputStr);

        const fetchData = async () => {
            setLoading(true);
            try {
                console.log('API 호출 시작');

                //  영화 데이터 가져오기
                const movieResponse = await axiosInstance.get(`/movies/search/searchMovie`, {
                    params: {
                        page: 1,
                        pageSize: 20,
                        inputStr: inputStr
                    },
                });
                const movieData = await movieResponse.data.movieList;
                console.log('영화 데이터:', movieData);
                setMovies(movieData || []);

                // 도서 데이터 가져오기
                const bookResponse = await axiosInstance.get(`/books/search/searchBook`, {
                    params: {
                        page: 1,
                        pageSize : 20,
                        inputStr: inputStr
                    }
                })

                const bookData = await bookResponse.data.bookESDomainList;
                setBooks(bookData || []);

                console.log('도서 데이터 :: ' + bookData);
                setBooks(bookData || []);

            } catch (error) {
                console.error('데이터 가져오기 실패:', error);
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [inputStr]); // query가 변경될 때마다 useEffect 실행

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="search-body">
            <div className="search-section">
                <h2>{'"' + inputStr + '" 의 검색결과'}</h2>
            </div>
            <div className="search-section">
                <h2>영화</h2>
                <div className="more-link-container">

                    {movieList.length > 9 && (
                        <a>
                        <Link className="more-link" key={inputStr} to={`/movies/search/${inputStr}`}>더 보기</Link>
                        </a>
                    )}


                </div>
            </div>
            <div className="search-results movies">
                {movieList.slice(0, 9).map((movie) => (
                    <div key={movie.movieId} className="search-item">
                        <Link to={`/movie/${movie.movieId}`}>
                            <div className="movie-info">
                                <img src={movie.posterPath} alt={movie.title}/>
                                <div className="movie-info">
                                    <h3 className="movie-title">{movie.title}</h3>
                                    {renderStars(parseFloat(movie.voteAverage))}
                                    <span>({Math.round(parseFloat(movie.voteAverage) * 10) / 10})</span>
                                    <p className="movie-genres">
                                        {movie.movieGenre.map((g) => g.genreName).join(', ')}
                                    </p>
                                </div>
                            </div>
                        </Link>
                    </div>
                ))}
            </div>


            <div className="search-section">
                <h2>책</h2>
                <div className="more-link-container">
                    {books.length > 9 && (
                        <Link className="more-link" key={inputStr} to={`/books/search/${inputStr}`}>더 보기</Link>
                    )}
                </div>
            </div>
            <div className="search-results books">
                {books.slice(0, 9).map((book) => (
                    <div key={book.bookId} className="search-item">
                        <Link to={`/book/${book.bookId}`}>
                            <img src={book.bookImgUrl} alt={book.title}/>
                            <p>{book.title}</p>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}


export default SearchPage;