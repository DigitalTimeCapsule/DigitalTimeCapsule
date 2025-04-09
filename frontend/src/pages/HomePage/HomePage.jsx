// src/pages/HomePage.js
import React from "react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";
import "./HomePage.css";

const HomePage = () => {
  return (
    <div className="fullscreen-container">
      <motion.div
        className="centered-box"
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.6 }}
      >
        <h1 className="main-title">📦 Digital Time Capsule</h1>
        <p className="main-subtitle">
          Write to your future self. Save a message or a memory.
        </p>

        <div className="quick-info">
          <p>📝 Create a message</p>
          <p>📅 Set a future open date</p>
          <p>🔓 Come back when it’s ready</p>
        </div>

        <Link to="/login" className="action-button">
          🚀 Log In
        </Link>
      </motion.div>
    </div>
  );
};

export default HomePage;
