// src/components/MovieCarousel.js
import React from 'react';
import { Link } from "react-router-dom";
import './MovieHome.css';

function MovieCarousel({ title, movies, startIndex, handleNext, handlePrev, hasMore, loading, slideSize = 5 }) {
    return (
        <div className="movie-carousel-section">
            <h2>{title}</h2>
            <div className="movie-carousel">
                {startIndex > 0 && (
                    <button className="prev-button" onClick={handlePrev} aria-label="Previous">
                        {'<'}
                    </button>
                )}
                <div className="movie-list">
                    {movies.slice(startIndex, startIndex + slideSize).map((movie, index) => (
                        <Link className="movie-card" key={movie.movieId} to={`/movie/${movie.movieId}`}>
                            <div className="movie-rank">{startIndex + index + 1}</div>
                            <img
                                src={movie.posterPath || '/default-poster.jpg'}
                                alt={movie.title || 'No Title'}
                                className="movie-image"
                            />
                            <div className="movie-info">
                                <h3 className="movie-title">{movie.title}</h3>
                                <span>({Math.round(parseFloat(movie.voteAverage) * 10) / 10})</span>
                                <p className="movie-genres">
                                    {movie.movieGenreList.map((g) => g.genreName).join(', ')}
                                </p>
                            </div>
                        </Link>
                    ))}
                </div>
                {(startIndex + slideSize < movies.length || hasMore) && (
                    <button
                        className="next-button"
                        onClick={handleNext}
                        aria-label="Next"
                        disabled={loading}
                    >
                        {'>'}
                    </button>
                )}
                {loading && <p>Loading more movies...</p>}
            </div>
        </div>
    );
}

export default MovieCarousel;