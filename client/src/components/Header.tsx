// components/Header.tsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from '../styles/Header.module.css';
import { motion } from 'framer-motion';
import { logout } from '../services/authService';

interface HeaderProps {
    isLoggedIn: boolean;
}

const Header: React.FC<HeaderProps> = ({ isLoggedIn }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <header className={styles.header}>
            <nav className={styles.nav}>
                <motion.div
                    className={styles.logoContainer}
                    initial={{ opacity: 0, x: -50 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 1, ease: 'easeInOut' }}
                >
                    <Link to="/">
                        <img src="src/assets/logo.png" alt="Logo" className={styles.logo} />
                    </Link>
                </motion.div>
                <motion.div
                    className={styles.linksContainer}
                    initial={{ opacity: 0, x: 50 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 1, ease: 'easeInOut', delay: 0.2 }}
                >
                    {isLoggedIn ? (
                        <button onClick={handleLogout} className={styles.logoutButton}>
                            Logout
                        </button>
                    ) : (
                        <Link to="/login" className={styles.link}>
                            <div className={styles.userIcon} />
                        </Link>
                    )}
                </motion.div>
            </nav>
        </header>
    );
};

export default Header;