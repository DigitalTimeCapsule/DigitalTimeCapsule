import React, { useState } from "react";
import axios from "axios";
import { set, useForm } from "react-hook-form";
import "./login.css";

const Login = () => {
    const [isSignup, setIsSignup] = useState(false);
    const [isTransitioning, setIsTransitioning] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm();

    const handleToggle = () => {
        setIsTransitioning(true);
        setTimeout(() => {
            setIsSignup(!isSignup);
            setIsTransitioning(false);
        }, 300); // Matches CSS animation duration
    };

    const handleLogin = async (data) => {
        try {
            await axios.post("http://localhost:8080/api/users/login", {
                email: data.email,
                password: data.password,
            });
            alert("Login successful");
            reset();
        } catch (error) {
            alert(error.response?.data?.message || "Login failed");
        }
    };

    const handleSignup = async (data) => {
        if (data.password !== data.confirmPassword) {
            alert("Passwords do not match");
            return;
        }

        try {
            await axios.post("http://localhost:8080/api/users/register", {
                email: data.email,
                password: data.password,
            });

            alert("Signup successful");
            setIsSignup(!isSignup);
            reset();
        } catch (error) {
            alert(error.response?.data || "Signup failed");
        }
    };

    return (
        <div className="container">
            <div
                className={`form-box ${
                    isTransitioning ? "fade-out" : "fade-in"
                }`}
            >
                <h2>{isSignup ? "Sign Up" : "Login"}</h2>
                <form
                    onSubmit={handleSubmit(
                        isSignup ? handleSignup : handleLogin
                    )}
                >
                    <div className="input-group">
                        <label>Email</label>
                        <input
                            type="email"
                            {...register("email", {
                                required: "Email is required",
                                pattern: {
                                    value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
                                    message: "Invalid email format",
                                },
                            })}
                        />
                        {errors.email && (
                            <p className="error">{errors.email.message}</p>
                        )}
                    </div>

                    <div className="input-group">
                        <label>Password</label>
                        <input
                            type="password"
                            {...register("password", {
                                required: "Password is required",
                                minLength: {
                                    value: 6,
                                    message:
                                        "Password must be at least 6 characters",
                                },
                            })}
                        />
                        {errors.password && (
                            <p className="error">{errors.password.message}</p>
                        )}
                    </div>

                    {isSignup && (
                        <div className="input-group">
                            <label>Confirm Password</label>
                            <input
                                type="password"
                                {...register("confirmPassword", {
                                    required: "Please confirm your password",
                                    minLength: {
                                        value: 6,
                                        message:
                                            "Password must be at least 6 characters",
                                    },
                                })}
                            />
                            {errors.confirmPassword && (
                                <p className="error">
                                    {errors.confirmPassword.message}
                                </p>
                            )}
                        </div>
                    )}

                    <button type="submit" className="btn">
                        {isSignup ? "Sign Up" : "Login"}
                    </button>
                </form>

                <p className="toggle-text">
                    {isSignup
                        ? "Already have an account?"
                        : "Don't have an account?"}{" "}
                    <span className="toggle-link" onClick={handleToggle}>
                        {isSignup ? "Login" : "Sign Up"}
                    </span>
                </p>
            </div>
        </div>
    );
};

export default Login;
