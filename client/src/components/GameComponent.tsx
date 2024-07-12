import React, { useState, useEffect } from 'react';
import styles from '../styles/GamePage.module.css';
import GameBoard3D from '../components/GameBoard3D';
import { GameField } from '../interfaces/GameField';
import LevelOverlay from '../components/LevelOverlay';
import { AnimatePresence } from 'framer-motion';

interface GameComponentProps {
    gameField: GameField | null;
    setLevelCompleted: (completed: boolean) => void;
    setGameCompleted: (completed: boolean) => void;
}

const GameComponent: React.FC<GameComponentProps> = ({
                                                         gameField,
                                                         setGameCompleted,
                                                         setLevelCompleted,
                                                     }) => {
    const [showLevelOverlay, setShowLevelOverlay] = useState(false);
    const [currentLevel, setCurrentLevel] = useState(1);

    useEffect(() => {
        if (showLevelOverlay) {
            const timer = setTimeout(() => {
                setShowLevelOverlay(false);
                setCurrentLevel((prevLevel) => prevLevel + 1);
            }, 1000);

            return () => clearTimeout(timer);
        }
    }, [showLevelOverlay]);

    const handleLevelCompletion = () => {
        setShowLevelOverlay(true);
        setLevelCompleted(true);
    };

    const handleGameCompletion = () => {
        setGameCompleted(true);
    };

    return (
        <main className={styles.main}>
            <div className={styles.gameContent}>
                {gameField && (
                    <GameBoard3D
                        gameField={gameField}
                        setLevelCompleted={handleLevelCompletion}
                        setGameCompleted={handleGameCompletion}
                    />
                )}
                <AnimatePresence>
                    {showLevelOverlay && (
                        <LevelOverlay currentLevel={currentLevel} />
                    )}
                </AnimatePresence>
            </div>
        </main>
    );
};

export default GameComponent;