import React, {useState} from "react";
import {useForm} from "react-hook-form";
import axios from "axios";
import {motion} from "framer-motion";
import "./CreateCapsulePage.css";

const CreateCapsulePage = () => {
    const {register, handleSubmit, reset, watch, formState: {errors}} = useForm();
    const [statusMessage, setStatusMessage] = useState("");
    const capsuleType = watch("type", "text"); // default to 'text'

    const onSubmit = async (data) => {
        const currentDate = new Date();
        const openDate = new Date(currentDate);
        openDate.setFullYear(currentDate.getFullYear() + 5);

        const formData = new FormData();
        formData.append("title", data.title);
        formData.append("type", data.type);
        formData.append("openDate", openDate.toISOString());

        if (data.type === "text") {
            formData.append("message", data.message);
        } else if (data.type === "image" || data.type === "video") {
            if (data.file[0]) {
                formData.append("file", data.file[0]);
            } else {
                setStatusMessage("❌ Please upload a file.");
                return;
            }
        }

        try {
            await axios.post("http://localhost:8080/api/capsules", formData, {
                headers: {"Content-Type": "multipart/form-data"},
            });

            setStatusMessage("🎉 Capsule created successfully!");
            reset();
        } catch (error) {
            setStatusMessage("❌ Failed to create capsule. Please try again.");
            console.error(error);
        }
    };

    return (
        <div className="capsule-container">
            <motion.div
                className="form-card"
                initial={{opacity: 0, y: 20}}
                animate={{opacity: 1, y: 0}}
                transition={{duration: 0.5}}
            >
                <h2>Create a New Time Capsule</h2>
                <form onSubmit={handleSubmit(onSubmit)} className="capsule-form">
                    <div className="input-group">
                        <label>Title</label>
                        <input
                            type="text"
                            {...register("title", {required: "Title is required"})}
                        />
                        {errors.title && <p className="error">{errors.title.message}</p>}
                    </div>

                    <div className="input-group">
                        <label>Type</label>
                        <select {...register("type")}>
                            <option value="text">Text</option>
                            <option value="image">Image</option>
                            <option value="video">Video</option>
                        </select>
                    </div>

                    {capsuleType === "text" && (
                        <div className="input-group">
                            <label>Message</label>
                            <textarea
                                rows={5}
                                {...register("message", {
                                    required: "Message is required for text capsules",
                                })}
                            />
                            {errors.message && <p className="error">{errors.message.message}</p>}
                        </div>
                    )}

                    {(capsuleType === "image" || capsuleType === "video") && (
                        <div className="input-group">
                            <label>{capsuleType === "image" ? "Upload Image" : "Upload Video"}</label>
                            <input
                                type="file"
                                accept={capsuleType === "image" ? "image/*" : "video/*"}
                                {...register("file", {required: "File is required"})}
                            />
                            {errors.file && <p className="error">{errors.file.message}</p>}
                        </div>
                    )}

                    <button type="submit" className="btn">📦 Create Capsule</button>
                </form>

                {statusMessage && <p className="status-message">{statusMessage}</p>}
            </motion.div>
        </div>
    );
};

export default CreateCapsulePage;
