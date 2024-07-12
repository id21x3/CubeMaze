import React, {useEffect, useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import styles from '../styles/HomePage.module.css';
import { motion } from 'framer-motion';
import { checkLoginStatus } from '../services/authService';

const HomePage: React.FC = () => {
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
                <motion.h1
                    className={styles.title}
                    initial={{ opacity: 0, y: -50 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 1, ease: 'easeInOut' }}
                >
                    CUBEMAZE
                </motion.h1>
                <motion.div
                    className={styles.playButton}
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    transition={{ duration: 0.8, ease: 'easeInOut', delay: 0.5 }}
                >
                    <Link to="/game">
                        <motion.div
                            className={styles.playIcon}
                            whileHover={{ scale: 1.1 }}
                            whileTap={{ scale: 0.9 }}
                        />
                    </Link>
                    <span className={styles.playText}>PLAY</span>
                </motion.div>
            </main>
            <Footer />
        </div>
    );
};

export default HomePage;
