import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import axios from "axios";
import config from "../config";

const PrivateRoute = ({ children }) => {
    const [isValid, setIsValid] = useState(null);

    useEffect(() => {
        const checkToken = async () => {
            const token = localStorage.getItem("authToken");
            if (!token) {
                setIsValid(false);
                return;
            }

            try {
                await axios.get(`${config.apiUrl}/users/validate`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setIsValid(true);
            } catch (error) {
                console.error("Invalid token:", error);
                localStorage.removeItem("authToken");
                setIsValid(false);
            }
        };

        checkToken();
    }, []);

    if (isValid === null) {
        return <div>Loading...</div>;
    }

    return isValid ? children : <Navigate to="/login" replace />;
};

export default PrivateRoute;
