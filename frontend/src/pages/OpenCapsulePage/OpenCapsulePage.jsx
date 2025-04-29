import React, {useEffect, useState} from "react";
import axios from "axios";
import Modal from "react-modal";
import {useNavigate} from "react-router-dom";
import "./OpenCapsulePage.css";

Modal.setAppElement("#root");

const OpenCapsulePage = () => {
    const [capsules, setCapsules] = useState([]);
    const [selectedCapsule, setSelectedCapsule] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUnopenedCapsules = async () => {
            try {
                const token = localStorage.getItem("authToken");
                const response = await axios.get("http://localhost:8080/api/capsules/unopened", {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setCapsules(response.data);
            } catch (error) {
                console.error("Failed to fetch unopened capsules:", error);
            }
        };

        fetchUnopenedCapsules();
    }, []);

    const fixPath = (path) => {
        console.log(path)
        return "http://localhost:8080/" + path.replace(/\\/g, '/');
    };

    return (
        <div className="capsule-open-page-container">
            <button className="return-btn" onClick={() => navigate("/capsule-manager")}>
                ⬅️ Return to Capsule Manager
            </button>
            <h2>🔒 Open Capsules</h2>
            <div className="capsule-list">
                {capsules.length > 0 && capsules.map((capsule) => {
                    const isLocked = new Date(capsule.expiryDateTime) > new Date();

                    return (
                        <div
                            key={capsule.id}
                            className={`capsule-card ${isLocked ? 'locked' : ''}`}
                            onClick={() => {
                                if (!isLocked) {
                                    setSelectedCapsule(capsule);

                                    const token = localStorage.getItem("authToken");
                                    axios.put(`http://localhost:8080/api/capsules/${capsule.id}/mark-opened`, {}, {
                                        headers: {Authorization: `Bearer ${token}`}
                                    }).then(() => {
                                        setCapsules((prev) => prev.filter((c) => c.id !== capsule.id));
                                    }).catch((err) => console.error("Failed to mark capsule as opened:", err));
                                }
                            }}
                            style={{cursor: isLocked ? "not-allowed" : "pointer", opacity: isLocked ? 0.6 : 1}}
                        >
                            <h3>{capsule.name}</h3>
                            <p>Opens on: {new Date(capsule.expiryDateTime).toLocaleString('en-GB', {
                                day: '2-digit',
                                month: 'short',
                                year: 'numeric',
                                hour: '2-digit',
                                minute: '2-digit',
                                hour12: false
                            })}
                            </p>
                            {isLocked && <p style={{color: "#d00"}}>🔒 Locked</p>}
                        </div>
                    );
                })}
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
                        <h2>{selectedCapsule.name}</h2>

                        {/* Group data types */}
                        {selectedCapsule.capsuleDataList?.filter(data => data.dataType === "text").map((textData, index) => (
                            <div key={index}>
                                <h4>📩 Message</h4>
                                <p>{textData.content}</p>
                            </div>
                        ))}

                        {selectedCapsule.capsuleDataList?.filter(data => data.dataType === "image").length > 0 && (
                            <>
                                <h4>🖼️ Images</h4>
                                <div className="media-row">
                                    {selectedCapsule.capsuleDataList
                                        .filter(data => data.dataType === "image")
                                        .map((imageData, index) => (
                                            <img key={index} src={fixPath(imageData.content)} alt={`img-${index}`}/>
                                        ))}
                                </div>
                            </>
                        )}

                        {selectedCapsule.capsuleDataList?.filter(data => data.dataType === "video").length > 0 && (
                            <>
                                <h4>🎥 Videos</h4>
                                <div className="media-row">
                                    {selectedCapsule.capsuleDataList
                                        .filter(data => data.dataType === "video")
                                        .map((videoData, index) => (
                                            <video key={index} controls width="250">
                                                <source src={fixPath(videoData.content)} type="video/mp4"/>
                                            </video>
                                        ))}
                                </div>
                            </>
                        )}

                        {selectedCapsule.capsuleDataList?.filter(data => data.dataType === "file").length > 0 && (
                            <>
                                <h4>📎 Files</h4>
                                <ul>
                                    {selectedCapsule.capsuleDataList
                                        .filter(data => data.dataType === "file")
                                        .map((fileData, index) => (
                                            <li key={index}>
                                                <a href={fixPath(fileData.content)} target="_blank" rel="noreferrer">
                                                    Download File {index + 1}
                                                </a>
                                            </li>
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

export default OpenCapsulePage;
