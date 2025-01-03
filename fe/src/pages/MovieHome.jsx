import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './MovieHome.css';

function MovieHome() {
    const [populars, setPopulars] = useState([]);
    const [startPopularIndex, setStartPopularIndex] = useState(0);
    const [latests, setLatests] = useState([]);
    const [startLatestIndex, setStartLatestIndex] = useState(0);

    // 인기 많은 영화
    useEffect(() => {
        const fetchMovieListPopular = async () => {
            try {
                const response = await axios.get('/api/movies/main/popular', {
                    params: {
                        page: 1,
                        pageSize: 20
                    }, // 30개 데이터 한번에 가져오기
                });
                setPopulars(response.data.movieList);
            } catch (error) {
                console.error('Error fetch movieListPopular:', error);
            }
        };

        fetchMovieListPopular();
    }, []);

    const handleNext = () => {
        if (startPopularIndex + 5 < populars.length) {
            setStartPopularIndex(startPopularIndex + 5);
        }
    };

    const handlePrev = () => {
        if (startPopularIndex > 0) {
            setStartPopularIndex(startPopularIndex - 5);
        }
    };

    // 최신 인기 영화
    useEffect(() => {
        const fetchMovieListLatest = async () => {
            try {
                const response = await axios.get('/api/movies/main/latest', {
                    params: {
                        page: 1,
                        pageSize: 20
                    }, // 30개 데이터 한번에 가져오기
                });
                setLatests(response.data.movieList);
            } catch (error) {
                console.error('Error fetch movieListLatest:', error);
            }
        };

        fetchMovieListLatest();
    }, []);

    const latestHandleNext = () => {
        if (startLatestIndex + 5 < latests.length) {
            setStartLatestIndex(startLatestIndex + 5);
        }
    };

    const latestHandlePrev = () => {
        if (startLatestIndex > 0) {
            setStartLatestIndex(startLatestIndex - 5);
        }
    };

    return (
        <div className="book-home">
            <h2>인기 많은 영화</h2>
            <div className="book-carousel">
                {startPopularIndex > 0 && (
                    <button className="prev-button" onClick={handlePrev}>
                        {'<'}
                    </button>
                )}
                <div className="book-list">
                    {populars.slice(startPopularIndex, startPopularIndex + 5).map((popular, index) => (
                        <div className="book-card" key={popular.movieId}>
                            <div className="book-rank">{startPopularIndex + index + 1}</div>
                            <img src={popular.posterPath} alt={popular.title} className="book-image"/>
                            <div className="book-info">
                                <h3 className="book-title">{popular.title}</h3>
                                {/* TODO : 태그, 장르 가져오기*/}
                                {/*<p className="book-writer">*/}
                                {/*    {movie.tagList.map((tag) => tag.name).join(', ')}*/}
                                {/*</p>*/}
                            </div>
                        </div>
                    ))}
                </div>
                {startPopularIndex + 5 < populars.length && (
                    <button className="next-button" onClick={handleNext}>
                        {'>'}
                    </button>
                )}
            </div>

            <h2>최신 인기 영화</h2>
            <div className="book-carousel">
                {startLatestIndex > 0 && (
                    <button className="prev-button" onClick={latestHandlePrev}>
                        {'<'}
                    </button>
                )}
                <div className="book-list">
                    {latests.slice(startLatestIndex, startLatestIndex + 5).map((latest, index) => (
                        <div className="book-card" key={latest.movieId}>
                            <div className="book-rank">{startLatestIndex + index + 1}</div>
                            <img src={latest.posterPath} alt={latest.title} className="book-image"/>
                            <div className="book-info">
                                <h3 className="book-title">{latest.title}</h3>
                                {/* TODO : 태그, 장르 가져오기*/}
                                {/*<p className="book-writer">*/}
                                {/*    {movie.tagList.map((tag) => tag.name).join(', ')}*/}
                                {/*</p>*/}
                            </div>
                        </div>
                    ))}
                </div>
                {startLatestIndex + 5 < latests.length && (
                    <button className="next-button" onClick={latestHandleNext}>
                        {'>'}
                    </button>
                )}
            </div>
        </div>


    );


}

export default MovieHome;
