import React, {useEffect, useState} from "react";
import axios from "axios";
import Modal from "react-modal";
import {useNavigate} from "react-router-dom";
import "./OpenCapsulePage.css";

Modal.setAppElement("#root");

const OpenCapsulePage = () => {
    const [capsules, setCapsules] = useState([{
        id: 1,
        title: "Graduation Message",
        message: "Congratulations! You've made it to the end!",
        imageUrls: ["https://picsum.photos/200?random=1"],
        videoUrls: ["https://www.w3schools.com/html/mov_bbb.mp4"],
        fileUrls: ["https://www.orimi.com/pdf-test.pdf"],
        openDate: "2024-12-25T10:00:00"
    },
        {
            id: 2,
            title: "Time Capsule from 2022",
            message: "I hope you're doing well and following your dreams.",
            imageUrls: [
                "https://picsum.photos/200?random=2",
                "https://picsum.photos/200?random=3"
            ],
            videoUrls: [],
            fileUrls: [],
            openDate: "2027-06-10T09:30:00"
        },
        {
            id: 3,
            title: "Dream Journal",
            message: "Here are the things I wanted to achieve in 5 years.",
            imageUrls: [],
            videoUrls: [],
            fileUrls: [
                "https://file-examples.com/storage/fed137caae12a9ea6a4b5e6/2017/10/file-sample_150kB.pdf"
            ],
            openDate: "2026-05-15T14:00:00"
        }]);
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
    }, [capsules]);

    return (
        <div className="capsule-open-page-container">
            <button className="return-btn" onClick={() => navigate("/capsule-manager")}>
                ⬅️ Return to Capsule Manager
            </button>
            <h2>🔒 Open Capsules</h2>
            <div className="capsule-list">
                {capsules.map((capsule) => {
                    const isLocked = new Date(capsule.openDate) > new Date();

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
                            <h3>{capsule.title}</h3>
                            <p>Opens on: {new Date(capsule.openDate).toLocaleDateString()}</p>
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
                                        <img key={i} src={url} alt={`img-${i}`}/>
                                    ))}
                                </div>
                            </>
                        )}

                        {selectedCapsule.videoUrls?.length > 0 && (
                            <>
                                <h4>🎥 Videos</h4>
                                <div className="media-row">
                                    {selectedCapsule.videoUrls.map((url, i) => (
                                        <video key={i} controls width="250">
                                            <source src={url} type="video/mp4"/>
                                        </video>
                                    ))}
                                </div>
                            </>
                        )}

                        {selectedCapsule.fileUrls?.length > 0 && (
                            <>
                                <h4>📎 Files</h4>
                                <ul>
                                    {selectedCapsule.fileUrls.map((url, i) => (
                                        <li key={i}><a href={url} target="_blank" rel="noreferrer">Download
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

export default OpenCapsulePage;
