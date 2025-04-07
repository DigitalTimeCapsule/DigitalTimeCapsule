import React from "react";
import {useForm} from "react-hook-form";
import axios from "axios";

const SignupForm = ({onToggle}) => {
    const {register, handleSubmit, reset, formState: {errors}} = useForm();

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
            reset();
            onToggle(); // Return to login
        } catch (error) {
            alert(error.response?.data || "Signup failed");
        }
    };

    return (
        <div className="form-box fade-in">
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit(handleSignup)}>
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

                <div className="input-group">
                    <label>Confirm Password</label>
                    <input
                        type="password"
                        {...register("confirmPassword", {
                            required: "Please confirm your password",
                            minLength: {
                                value: 6,
                                message: "Password must be at least 6 characters",
                            },
                        })}
                    />
                    {errors.confirmPassword && (
                        <p className="error">{errors.confirmPassword.message}</p>
                    )}
                </div>

                <button type="submit" className="btn">Sign Up</button>
            </form>

            <p className="toggle-text">
                Already have an account?{" "}
                <span className="toggle-link" onClick={onToggle}>Login</span>
            </p>
        </div>
    );
};

export default SignupForm;
