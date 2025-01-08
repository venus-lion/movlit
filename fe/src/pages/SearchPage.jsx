import {useEffect, useState} from "react";

import { useSearchParams, Link } from 'react-router-dom';
import './searchPage.css';

function SearchPage() {
    const [searchParams] = useSearchParams();
    const query = searchParams.get('q'); // 쿼리 파라미터에서 검색어 가져오기
    const [movieList, setMovies] = useState([]);
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        console.log('useEffect 실행됨, query:', query);

        const fetchData = async () => {
            setLoading(true);
            try {
                console.log('API 호출 시작');

                //  영화 데이터 가져오기
                const movieResponse = await fetch(`/api/movies/main/popular`);
                const movieData = await movieResponse.json();
                console.log('영화 데이터:', movieData);
                setMovies(movieData.movieList || []);

                // 도서 데이터 가져오기
                const bookResponse = await fetch(`/api/books/bestseller`);
                const bookData = await bookResponse.json();
                console.log('bookData :: ' + bookData);
                console.log('도서 데이터:', bookData);
                setBooks(bookData.books || []);

            } catch (error) {
                console.error('데이터 가져오기 실패:', error);
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [query]); // query가 변경될 때마다 useEffect 실행

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="search-body">
            <h1>{query || '의 검색결과'}</h1>

            <div className="search-section">
                <h2>영화</h2>
                <div className="more-link-container">
                    {movieList.length > 9 && (
                        <Link className="more-link" to={`/movies/search?query=${query}`}>더 보기</Link>
                    )}
                </div>
            </div>
            <div className="search-results movies">
                {movieList.slice(0, 9).map((movie) => (
                    <div key={movie.movieId} className="search-item">
                        <Link to={`/movie/${movie.movieId}`}>
                            <img src={movie.posterPath} alt={movie.title}/>
                            <p>{movie.title}</p>
                        </Link>
                    </div>
                ))}
            </div>


            <div className="search-section">
                <h2>책</h2>
                <div className="more-link-container">
                    {books.length > 9 && (
                        <Link className="more-link" to={`/books/search?query=${query}`}>더 보기</Link>
                    )}
                </div>
            </div>
            <div className="search-results books">
                {books.slice(0, 9).map((book) => (
                    <div key={book.bookId} className="search-item">
                        <Link to={`/book/${book.bookId}`}>
                            <img src={book.bookImgUrl} alt={book.title} />
                            <p>{book.title}</p>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}
//     return (
//         <div className="search-body">
//             <h1>{query || '의 검색결과'}</h1>
//
//             <h2 >영화</h2>
//             <div className="component-container">
//                 <div className="search-results movies">
//                     {movieList.slice(0, 9).map((movie) => (
//                         <div key={movie.movieId} className="search-item">
//                             <Link to={`/movie/${movie.movieId}`}>
//                                 <img src={movie.posterPath} alt={movie.title} />
//                                 <h3>{movie.title}</h3>
//                             </Link>
//                         </div>
//                     ))}
//                 </div>
//                 {movieList.length > 9 && (
//                     <Link to={`/movies/search?query=${query}`}>더 보기</Link>
//                 )}
//             </div>
//
//             <h2>책</h2>
//             <div className="component-container">
//                 <div className="search-results books">
//                     {books.slice(0, 9).map((book) => (
//                         <div key={book.bookId} className="search-item">
//                             <Link to={`/book/${book.bookId}`}>
//                                 <img src={book.bookImgUrl} alt={book.title} />
//                                 <h3>{book.title}</h3>
//                             </Link>
//                         </div>
//                     ))}
//                     {books.length > 9 && (
//                         <Link to={`/books/search?query=${query}`}>더 보기</Link>
//                     )}
//                 </div>
//             </div>
//         </div>
//     );
// }




export default SearchPage;
