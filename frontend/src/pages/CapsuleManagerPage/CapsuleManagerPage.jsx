import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import "./CapsuleManagerPage.css";
import { FiPlusCircle } from "react-icons/fi";
import { FcViewDetails, FcUnlock } from "react-icons/fc";
import axios from "axios";
import config from "../../config";

const CapsuleManagerPage = () => {
    const navigate = useNavigate();
    const [numOfUnopened, setNumOfUnopened] = useState();
    const [numOfOpened, setNumOfOpened] = useState();

    useEffect(() => {
        const fetchCapsules = async () => {
            try {
                const token = localStorage.getItem("authToken")
                const response = await axios.get(`${config.apiUrl}/capsules`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                console.log(response.data)
                const opened = response.data.filter(capsule => capsule.opened).length;
                const unopened = response.data.filter(capsule => !capsule.opened).length;

                setNumOfOpened(opened);
                setNumOfUnopened(unopened);
            } catch (error) {
                console.error("Fetch Unopened Capsules Error: ", error);
            }
        }

        fetchCapsules();
    }, [numOfOpened]);

    return (<div className="capsule-manager-container">
        <motion.div
            className="capsule-manager-card"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
        >
            <h2 className="capsule-manager-title">🎁 Capsule Manager</h2>
            <div className="capsule-manager-options">
                <button onClick={() => navigate("/capsule/create")} className="capsule-manager-btn">
                    <FiPlusCircle /><span>Create New Capsule</span>
                </button>
                <button onClick={() => navigate("/capsule/history")} className="capsule-manager-btn">
                    <FcViewDetails /><span>View Older Capsules</span>{numOfOpened}
                </button>
                <button onClick={() => navigate("/capsule/open")} className="capsule-manager-btn">
                    <FcUnlock /><span>Open Capsules</span>{numOfUnopened}
                </button>
            </div>
        </motion.div>
    </div>);
};

export default CapsuleManagerPage;
