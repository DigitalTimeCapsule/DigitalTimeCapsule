import React, {useState} from "react";
import {useForm} from "react-hook-form";
import axios from "axios";
import {motion} from "framer-motion";
import "./CreateCapsulePage.css";
import {useNavigate} from "react-router-dom";

const CreateCapsulePage = () => {
    const {register, handleSubmit, reset, formState: {errors}} = useForm();
    const [statusMessage, setStatusMessage] = useState("");
    const navigate = useNavigate();

    const [imageFiles, setImageFiles] = useState([]);
    const [videoFiles, setVideoFiles] = useState([]);
    const [otherFiles, setOtherFiles] = useState([]);

    let today = new Date();
    today.setDate(today.getDate() + 1);
    const tomorrow = today.toISOString().slice(0, 16)

    const handleFileChange = (e, setter, currentFiles) => {
        const newFiles = Array.from(e.target.files);
        setter([...currentFiles, ...newFiles]);
    };

    const onSubmit = async (data) => {

        const formData = new FormData();
        formData.append("title", data.title);
        formData.append("openDate", data.openDate);

        if (data.message) {
            formData.append("message", data.message);
        }

        imageFiles.forEach((file) => formData.append("images", file));
        videoFiles.forEach((file) => formData.append("videos", file));
        otherFiles.forEach((file) => formData.append("files", file));

        try {
            const token = localStorage.getItem("authToken");
            await axios.post("http://localhost:8080/api/capsules", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    Authorization: `Bearer ${token}`
                },
            });

            setStatusMessage("🎉 Capsule created successfully!");
            reset();
            setImageFiles([]);
            setVideoFiles([]);
            setOtherFiles([]);
        } catch (error) {
            setStatusMessage("❌ Failed to create capsule. Please try again.");
            console.error(error);
        }
    };

    const removeFile = (index, fileType) => {
        if (fileType === "images") {
            setImageFiles(imageFiles.filter((_, i) => i !== index));
        } else if (fileType === "videos") {
            setVideoFiles(videoFiles.filter((_, i) => i !== index));
        } else if (fileType === "files") {
            setOtherFiles(otherFiles.filter((_, i) => i !== index));
        }
    };


    return (
        <div className="capsule-container">
            <button className="return-btn" onClick={() => navigate("/capsule-manager")}>
                ⬅️ Return to Capsule Manager
            </button>
            <motion.div
                className="capsule-form-card"
                initial={{opacity: 0, y: 20}}
                animate={{opacity: 1, y: 0}}
                transition={{duration: 0.5}}
            >

                <h2 className="capsule-title">Create a New Time Capsule</h2>
                <form onSubmit={handleSubmit(onSubmit)} className="capsule-form">
                    <div className="capsule-input-group">
                        <label>Title</label>
                        <input
                            type="text"
                            {...register("title", {required: "Title is required"})}
                        />
                        {errors.title && <p className="capsule-error">{errors.title.message}</p>}
                    </div>

                    <div className="capsule-input-group">
                        <label>Message (Optional)</label>
                        <textarea
                            rows={5}
                            {...register("message")}
                            placeholder="Write a message..."
                        />
                    </div>

                    <div className="capsule-input-group">
                        <label>Add Image(s)</label>
                        <input
                            type="file"
                            accept="image/*"
                            onChange={(e) => handleFileChange(e, setImageFiles, imageFiles)}
                        />
                        <ul>
                            {imageFiles.map((file, i) => (
                                <li key={i}>
                                    {file.name}
                                    <button type="button" onClick={() => removeFile(i, "images")}>❌</button>
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div className="capsule-input-group">
                        <label>Add Video(s)</label>
                        <input
                            type="file"
                            accept="video/*"
                            onChange={(e) => handleFileChange(e, setVideoFiles, videoFiles)}
                        />
                        <ul>
                            {videoFiles.map((file, i) => (
                                <li key={i}>
                                    {file.name}
                                    <button type="button" onClick={() => removeFile(i, "videos")}>❌</button>
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div className="capsule-input-group">
                        <label>Add File(s)</label>
                        <input
                            type="file"
                            accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.txt"
                            onChange={(e) => handleFileChange(e, setOtherFiles, otherFiles)}
                        />
                        <ul>
                            {otherFiles.map((file, i) => (
                                <li key={i}>
                                    {file.name}
                                    <button type="button" onClick={() => removeFile(i, "files")}>❌</button>
                                </li>
                            ))}
                        </ul>
                    </div>


                    <div className="capsule-input-group">
                        <label>Expiry Date</label>
                        <input
                            type="datetime-local"
                            min={tomorrow}
                            {...register("openDate", {required: "Expiry Date is required"})}
                        />
                    </div>

                    <button type="submit" className="capsule-btn">📦 Create Capsule</button>
                </form>

                {statusMessage && (
                    <p className="capsule-status-message">{statusMessage}</p>
                )}
            </motion.div>
        </div>
    );
};

export default CreateCapsulePage;
