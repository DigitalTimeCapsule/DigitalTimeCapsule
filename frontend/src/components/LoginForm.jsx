import React from "react";
import {useForm} from "react-hook-form";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const LoginForm = ({onToggle}) => {
    const {register, handleSubmit, reset, formState: {errors}} = useForm();
    const navigate = useNavigate();
    const handleLogin = async (data) => {
        try {
            const response = await axios.post("http://localhost:8080/api/users/login", {
                email: data.email,
                password: data.password,
            });

            const token = response.data;
            localStorage.setItem("authToken", token);

            alert("Login successful");
            reset();
            navigate("/capsule-manager");
        } catch (error) {
            alert(error.response?.data?.message || "Login failed");
        }
    };

    return (
        <div className="form-box fade-in">
            <h2>Login</h2>
            <form onSubmit={handleSubmit(handleLogin)}>
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
                    {errors.email && <p className="error">{errors.email.message}</p>}
                </div>

                <div className="input-group">
                    <label>Password</label>
                    <input
                        type="password"
                        {...register("password", {
                            required: "Password is required",
                            minLength: {
                                value: 6,
                                message: "Password must be at least 6 characters",
                            },
                        })}
                    />
                    {errors.password && <p className="error">{errors.password.message}</p>}
                </div>

                <button type="submit" className="btn">Login</button>
            </form>

            <p className="toggle-text">
                Don't have an account?{" "}
                <span className="toggle-link" onClick={onToggle}>Sign Up</span>
            </p>
        </div>
    );
};

export default LoginForm;
