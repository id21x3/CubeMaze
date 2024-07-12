// pages/InfoPage.tsx
import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import GameInfo from '../components/GameInfo';
import styles from '../styles/InfoPage.module.css';
import { motion } from 'framer-motion';
import { checkLoginStatus } from '../services/authService';
import { useNavigate } from 'react-router-dom';

const InfoPage: React.FC = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
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

    return (
        <div className={styles.container}>
            <Header isLoggedIn={isLoggedIn} />
            <main className={styles.main}>
                <motion.div
                    className={styles.main}
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 1, ease: 'easeInOut' }}
                >
                    <GameInfo />
                </motion.div>
            </main>
            <Footer />
        </div>
    );
};

export default InfoPage;