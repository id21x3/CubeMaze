// LevelSelectionComponent.tsx
import React from 'react';
import styles from '../styles/LevelSelectionPage.module.css';
import { motion } from 'framer-motion';
import { Level } from '../interfaces/Level';

interface LevelSelectionComponentProps {
    levels: Level[];
    onLevelSelect: (levelIndex: number) => void;
}

const LevelSelectionComponent: React.FC<LevelSelectionComponentProps> = ({
                                                                             levels,
                                                                             onLevelSelect,
                                                                         }) => {
    return (
        <main className={styles.main}>
            <motion.div
                className={styles.main}
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 1, ease: 'easeInOut' }}
            >
                <h1 className={styles.title}>Select a level</h1>
                <div className={styles.levelGrid}>
                    {levels.map((level) => (
                        <div
                            key={level.index}
                            className={`${styles.levelIcon} ${
                                level.unlocked ? styles.unlocked : styles.locked
                            }`}
                            onClick={() => level.unlocked && onLevelSelect(level.index - 1)}
                        >
                            <img
                                src={level.unlocked ? level.icon : 'src/assets/level-icon-lock.png'}
                                alt={`Level ${level.index}`}
                                className={styles.levelImage}
                            />
                        </div>
                    ))}
                </div>
            </motion.div>
        </main>
    );
};

export default LevelSelectionComponent;
