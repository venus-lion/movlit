// useRecommendedBooks.jsx
import { useState, useEffect, useCallback } from 'react';
import axiosInstance from '../axiosInstance.js';

const UserRecommendedBooks = (isLoggedIn) => {
    const [recommendedBooks, setRecommendedBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchRecommendedBooks = useCallback(async () => {
        if (!isLoggedIn) {
            setLoading(false);
            return; // 로그인되지 않았으면 API 호출하지 않음
        }

        setLoading(true);
        try {
            const response = await axiosInstance.get('/books/recommendations');
            setRecommendedBooks(response.data);
            setError(null);
        } catch (error) {
            console.error('Error fetching recommended books:', error);
            setError(error);
        } finally {
            setLoading(false);
        }
    }, [isLoggedIn]);

    useEffect(() => {
        if (isLoggedIn) {
            fetchRecommendedBooks();
        }
    }, [isLoggedIn, fetchRecommendedBooks]);

    return { recommendedBooks, loading, error };
};

export default UserRecommendedBooks;