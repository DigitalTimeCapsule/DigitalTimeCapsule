import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './AuthenticatedMedia.css';

const AuthenticatedMedia = ({ src, type, alt, controls, width }) => {
    const [mediaUrl, setMediaUrl] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMedia = async () => {
            try {
                const token = localStorage.getItem('authToken');
                console.log('Token:', token);
                console.log('Request URL:', src);
                console.log('Request headers:', {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                });

                if (!token) {
                    setError('Authentication required. Please log in.');
                    return;
                }

                const response = await axios.get(src, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    responseType: 'blob'
                });

                console.log('Response headers:', response.headers);
                console.log('Response status:', response.status);

                const blob = new Blob([response.data], { type: response.headers['content-type'] });
                const url = URL.createObjectURL(blob);
                setMediaUrl(url);

                return () => {
                    URL.revokeObjectURL(url);
                };
            } catch (error) {
                console.error('Error fetching media:', error);
                console.error('Error response:', error.response);
                console.error('Error config:', error.config);

                if (error.response?.status === 403) {
                    // Try to validate the token
                    try {
                        const token = localStorage.getItem('authToken');
                        const validateResponse = await axios.get('http://localhost:8080/api/users/validate', {
                            headers: {
                                Authorization: `Bearer ${token}`
                            }
                        });
                        console.log('Token validation response:', validateResponse);
                    } catch (validateError) {
                        console.error('Token validation failed:', validateError);
                        setError('Your session has expired. Please log in again.');
                        localStorage.removeItem('authToken');
                        return;
                    }
                    setError('Access denied. Please log in again.');
                } else if (error.response?.status === 404) {
                    setError('File not found.');
                } else {
                    setError('Failed to load media. Please try again.');
                }
            }
        };

        fetchMedia();
    }, [src]);

    if (error) {
        return <div className="media-error">{error}</div>;
    }

    if (!mediaUrl) {
        return <div className="media-loading">Loading...</div>;
    }

    if (type === 'image') {
        return <img src={mediaUrl} alt={alt} width={width} />;
    } else if (type === 'video') {
        return (
            <video controls={controls} width={width}>
                <source src={mediaUrl} type="video/mp4" />
            </video>
        );
    }

    return null;
};

export default AuthenticatedMedia; 