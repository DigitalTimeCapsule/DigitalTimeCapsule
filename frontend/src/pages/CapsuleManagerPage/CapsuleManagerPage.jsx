import React from "react";
import {useNavigate} from "react-router-dom";
import {motion} from "framer-motion";
import "./CapsuleManagerPage.css";
import {FiPlusCircle} from "react-icons/fi";
import {FcViewDetails, FcUnlock} from "react-icons/fc";

const CapsuleManagerPage = () => {
    const navigate = useNavigate();

    return (
        <div className="capsule-manager-container">
            <motion.div
                className="capsule-manager-card"
                initial={{opacity: 0, y: 20}}
                animate={{opacity: 1, y: 0}}
                transition={{duration: 0.5}}
            >
                <h2 className="capsule-manager-title">🎁 Capsule Manager</h2>
                <div className="capsule-manager-options">
                    <button onClick={() => navigate("/capsule/create")} className="capsule-manager-btn">
                        <FiPlusCircle/><span>Create New Capsule</span>
                    </button>
                    <button onClick={() => navigate("/capsule/history")} className="capsule-manager-btn">
                        <FcViewDetails/><span>View Older Capsules</span>
                    </button>
                    <button onClick={() => navigate("/capsule/open")} className="capsule-manager-btn">
                        <FcUnlock/><span>Open Capsule</span>
                    </button>
                </div>
            </motion.div>
        </div>
    );
};

export default CapsuleManagerPage;
