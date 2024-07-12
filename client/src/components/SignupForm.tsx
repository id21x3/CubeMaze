// components/SignupForm.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signUp } from '../services/authService';
import styles from '../styles/SignupForm.module.css';
import { motion } from 'framer-motion';

const SignupForm: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            console.error('Пароли не совпадают');
            return;
        }
        try {
            const success = await signUp({ username, password, confirmPassword });
            if (success) {
                navigate('/login');
            } else {
                console.error('Ошибка регистрации');
            }
        } catch (error) {
            console.error('Ошибка регистрации:', error);
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
                <motion.input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className={styles.input}
                    initial={{ opacity: 0, x: -50 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.5, ease: 'easeInOut', delay: 0.6 }}
                />
                <motion.button
                    type="submit"
                    className={styles.button}
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    initial={{ opacity: 0, y: 50 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, ease: 'easeInOut', delay: 0.8 }}
                >
                    Sign Up
                </motion.button>
            </form>
        </motion.div>
    );
};

export default SignupForm;