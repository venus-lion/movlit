import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import {Link} from "react-router-dom";
import PopularMoviesComponent from "./PopularMoviesComponent.jsx";
import LatestMoviesComponent from "./LatestMoviesComponent.jsx";
import GenreMoviesComponent from "./GenreMoviesComponent.jsx";

function MovieHome() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [randomGenreIds, setRandomGenreIds] = useState([]);

    useEffect(() => {
        // 쿠키에서 refreshToken 값을 가져와서 로그인 상태 확인
        const checkLoginStatus = () => {
            const cookies = document.cookie.split(';');
            let refreshToken = null;

            cookies.forEach(cookie => {
                if (cookie.trim().startsWith('refreshToken=')) {
                    refreshToken = cookie.trim().split('=')[1];
                }
            });

            // refreshToken이 존재하면 로그인 상태로 설정
            if (refreshToken) {
                setIsLoggedIn(true);
            }
        };

        checkLoginStatus();

        // 1부터 16까지 숫자 중 랜덤하게 4개의 숫자 뽑기
        const getRandomGenreIds = () => {
            const genreIds = [];
            while (genreIds.length < 4) {
                const randomId = Math.floor(Math.random() * 16) + 1; // 1 ~ 16 사이의 랜덤 값
                if (!genreIds.includes(randomId)) {
                    genreIds.push(randomId);
                }
            }
            return genreIds;
        };


        setRandomGenreIds(getRandomGenreIds());
    }, []);

    return (
        <div className="movie-home">
            <PopularMoviesComponent />
            <LatestMoviesComponent />
            {!isLoggedIn && randomGenreIds.map(genreId => (
                <GenreMoviesComponent key={genreId} genreId={genreId} />
            ))}
        </div>
    );
}

export default MovieHome;
