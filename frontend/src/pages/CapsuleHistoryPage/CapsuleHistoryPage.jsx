import React, { useEffect, useState } from "react";
import axios from "axios";
import Modal from "react-modal";
import "./CapsuleHistoryPage.css";
import { useNavigate } from "react-router-dom";
import AuthenticatedMedia from "../../components/AuthenticatedMedia";
import config from "../../config";

Modal.setAppElement("#root");

const CapsuleHistoryPage = () => {
    const [capsules, setCapsules] = useState([]);
    const [selectedCapsule, setSelectedCapsule] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOpenedCapsules = async () => {
            try {
                const token = localStorage.getItem("authToken");
                const response = await axios.get(`${config.apiUrl}/capsules/opened`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setCapsules(response.data);
            } catch (error) {
                console.error("Failed to fetch capsules:", error);
            }
        };

        fetchOpenedCapsules();
    }, []);

    const fixPath = (path) => {
        // Extract just the filename from the path
        const filename = path.split(/[\\/]/).pop();
        return `${config.apiUrl}/files/${filename}`;
    };

    return (
        <div className="capsule-history-page-container">
            <button className="return-btn" onClick={() => navigate("/capsule-manager")}>
                ⬅️ Return to Capsule Manager
            </button>
            <h2>📜 Capsule History</h2>
            <div className="capsule-list">
                {capsules.map((capsule) => (
                    <div
                        key={capsule.id}
                        className="capsule-card"
                        onClick={() => setSelectedCapsule(capsule)}
                    >
                        <h3>{capsule.title}</h3>
                    </div>
                ))}
            </div>

            <Modal
                isOpen={!!selectedCapsule}
                onRequestClose={() => setSelectedCapsule(null)}
                contentLabel="Capsule Details"
                className="capsule-modal"
                overlayClassName="capsule-modal-overlay"
            >
                {selectedCapsule && (
                    <div className="capsule-modal-content">
                        <h2>{selectedCapsule.title}</h2>

                        {selectedCapsule.message && (
                            <>
                                <h4>📩 Message</h4>
                                <p>{selectedCapsule.message}</p>
                            </>
                        )}

                        {selectedCapsule.imageUrls?.length > 0 && (
                            <>
                                <h4>🖼️ Images</h4>
                                <div className="media-row">
                                    {selectedCapsule.imageUrls.map((url, i) => (
                                        <AuthenticatedMedia
                                            key={i}
                                            src={url}
                                            type="image"
                                            alt={`img-${i}`}
                                            width={250}
                                        />
                                    ))}
                                </div>
                            </>
                        )}

                        {selectedCapsule.videoUrls?.length > 0 && (
                            <>
                                <h4>🎥 Videos</h4>
                                <div className="media-row">
                                    {selectedCapsule.videoUrls.map((url, i) => (
                                        <AuthenticatedMedia
                                            key={i}
                                            src={url}
                                            type="video"
                                            controls={true}
                                            width={250}
                                        />
                                    ))}
                                </div>
                            </>
                        )}

                        {selectedCapsule.fileUrls?.length > 0 && (
                            <>
                                <h4>📎 Files</h4>
                                <ul>
                                    {selectedCapsule.fileUrls.map((url, i) => (
                                        <li key={i}><a href={fixPath(url)} target="_blank" rel="noreferrer">Download
                                            File {i + 1}</a></li>
                                    ))}
                                </ul>
                            </>
                        )}

                        <button onClick={() => setSelectedCapsule(null)}>❌ Close</button>
                    </div>
                )}
            </Modal>
        </div>
    );
};

export default CapsuleHistoryPage;
