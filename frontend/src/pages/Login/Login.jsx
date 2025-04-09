import React, {useState} from "react";
import LoginForm from "../../components/LoginForm";
import SignupForm from "../../components/SignupForm";
import "./login.css";

const Login = () => {
    const [isSignup, setIsSignup] = useState(false);

    const handleToggle = () => {
        setIsSignup(!isSignup);
    };

    return (
        <div className="login-container">
            {isSignup ? (
                <SignupForm onToggle={handleToggle}/>
            ) : (
                <LoginForm onToggle={handleToggle}/>
            )}
        </div>
    );
};

export default Login;
