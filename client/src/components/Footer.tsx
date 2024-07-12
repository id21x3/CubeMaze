import React from 'react';
import { Link } from 'react-router-dom';
import styles from '../styles/Footer.module.css';
import { motion } from 'framer-motion';

const Footer: React.FC = () => {
    return (
        <footer className={styles.footer}>
            <nav className={styles.nav}>
                <motion.div
                    className={styles.linksContainer}
                    initial={{ opacity: 0, y: 50 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 1, ease: 'easeInOut' }}
                >
                    <Link to="/" className={styles.link}>
                        <motion.div
                            className={styles.playIcon}
                            whileHover={{ scale: 1.1 }}
                            whileTap={{ scale: 0.9 }}
                        />
                        <span className={styles.linkText}></span>
                    </Link>
                    <Link to="/info" className={styles.link}>
                        <motion.div
                            className={styles.infoIcon}
                            whileHover={{scale: 1.1}}
                            whileTap={{scale: 0.9}}
                        />
                        <span className={styles.linkText}></span>
                    </Link>
                    <Link to="/login" className={styles.link}>
                        <motion.div
                            className={styles.userIcon}
                            whileHover={{scale: 1.1}}
                            whileTap={{scale: 0.9}}
                        />
                        <span className={styles.linkText}></span>
                    </Link>
                </motion.div>
            </nav>
        </footer>
    );
};

export default Footer;