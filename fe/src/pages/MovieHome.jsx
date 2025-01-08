import React, {useEffect, useState} from 'react';
import './Home.css';
import { Link, useOutletContext } from 'react-router-dom';
import PopularMoviesComponent from "./PopularMoviesComponent.jsx";
import LatestMoviesComponent from "./LatestMoviesComponent.jsx";
import GenreMoviesComponent from "./GenreMoviesComponent.jsx";
import InterestGenreMoviesComponent from "./InterestGenreMoviesComponent.jsx";
import RecentHeartSimilarCrewMoviesComponent from "./RecentHeartSimilarCrewMoviesComponent.jsx";

function MovieHome() {
    const [randomGenreIds, setRandomGenreIds] = useState([]);
    const { isLoggedIn } = useOutletContext();
    useEffect(() => {

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
            {isLoggedIn && <RecentHeartSimilarCrewMoviesComponent />}
            {isLoggedIn && <InterestGenreMoviesComponent />}
            {randomGenreIds.map(genreId => (
                <GenreMoviesComponent key={genreId} genreId={genreId} />
            ))}
        </div>
    );
}

export default MovieHome;
