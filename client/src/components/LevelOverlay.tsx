import React from 'react';
import { motion } from 'framer-motion';
import styles from '../styles/LevelOverlay.module.css';

interface LevelOverlayProps {
    currentLevel: number;
}

const LevelOverlay: React.FC<LevelOverlayProps> = ({ currentLevel }) => {
    return (
        <motion.div
            className={styles.overlay}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
        >
            <div className={styles.content}>
                <h2>Level {currentLevel+1}</h2>
            </div>
        </motion.div>
    );
};

export default LevelOverlay;