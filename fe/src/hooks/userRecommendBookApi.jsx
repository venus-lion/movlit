// useApiData.jsx
import { useState, useEffect, useCallback } from 'react';
import axiosInstance from '../axiosInstance';

const useApiData = (endpoint, isLoggedIn) => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchData = useCallback(async () => {
        if (!isLoggedIn) {
            setLoading(false);
            return;
        }

        setLoading(true);
        try {
            const response = await axiosInstance.get(endpoint);
            setData(response.data);
            setError(null);
        } catch (error) {
            console.error(`Error fetching data from ${endpoint}:`, error);
            setError(error);
        } finally {
            setLoading(false);
        }
    }, [isLoggedIn, endpoint]);

    useEffect(() => {
        if (isLoggedIn) {
            fetchData();
        }
    }, [isLoggedIn, fetchData]);

    return { data, loading, error };
};

export default useApiData;