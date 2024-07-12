// pages/LoginPage.tsx
import React, {useEffect, useState} from 'react';
import LoginForm from '../components/SignInForm';
import SignupForm from '../components/SignupForm';
import styles from '../styles/LoginPage.module.css';
import Header from "../components/Header.tsx";
import Footer from "../components/Footer.tsx";
import {checkLoginStatus} from "../services/authService.ts";
import {useNavigate} from "react-router-dom";

const LoginPage: React.FC = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [showLoginForm, setShowLoginForm] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const checkLogin = async () => {
            const loggedIn = await checkLoginStatus();
            setIsLoggedIn(loggedIn);
            if (!loggedIn) {
                navigate('/login');
            }
        };

        checkLogin();
    }, [navigate]);

    const toggleFormDisplay = () => {
        setShowLoginForm((prevState) => !prevState);
    };

    return (
        <div className={styles.container}>
            <Header isLoggedIn={isLoggedIn} />
                <div className={styles.loginFormsContainer}>
                    <div className={styles.formContainer}>
                        <div className={styles.formToggle}>
                            <button
                                onClick={toggleFormDisplay}
                                className={`${styles.toggleButton} ${showLoginForm ? styles.active : ''}`}
                            >
                                Sign In
                            </button>
                            <button
                                onClick={toggleFormDisplay}
                                className={`${styles.toggleButton} ${!showLoginForm ? styles.active : ''}`}
                            >
                                Sign Up
                            </button>
                        </div>
                        <div className={styles.formContent}>
                            {showLoginForm ? <LoginForm /> : <SignupForm />}
                        </div>
                    </div>
                </div>
            <Footer />
        </div>
    );
};

export default LoginPage;