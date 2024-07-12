import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signIn } from '../services/authService';
import styles from '../styles/SigninForm.module.css';
import { motion } from 'framer-motion';

const SignInForm: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const success = await signIn({ username, password });
            if (success) {
                navigate('/');
            } else {
                console.error('Ошибка авторизации');
            }
        } catch (error) {
            console.error('Ошибка авторизации:', error);
        }
    };

    return (
        <motion.div
            className={styles.FormContainer}
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5, ease: 'easeInOut' }}
        >
            <form onSubmit={handleSubmit} className={styles.form}>
                <motion.input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className={styles.input}
                    initial={{ opacity: 0, x: -50 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.5, ease: 'easeInOut', delay: 0.2 }}
                />
                <motion.input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className={styles.input}
                    initial={{ opacity: 0, x: 50 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.5, ease: 'easeInOut', delay: 0.4 }}
                />
                <motion.button
                    type="submit"
                    className={styles.button}
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    initial={{ opacity: 0, y: 50 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, ease: 'easeInOut', delay: 0.6 }}
                >
                    Login
                </motion.button>
            </form>
        </motion.div>
    );
};

export default SignInForm;