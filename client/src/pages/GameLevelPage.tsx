// pages/GameLevelPage.tsx
import React, { useState, useEffect } from 'react';
import {fetchLevels, saveGameState, selectMap} from '../services/gameService';
import LevelSelectionComponent from '../components/LevelSelectionComponent';
import GameComponent from '../components/GameComponent';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { Level } from '../interfaces/Level';
import { useNavigate } from 'react-router-dom';
import { checkLoginStatus } from '../services/authService';

const GameLevelPage: React.FC = () => {
    const [isLevelSelected, setIsLevelSelected] = useState(false);
    const [gameData, setGameData] = useState(null);
    const [levels, setLevels] = useState<Level[]>([]);
    const [lastLevel, setLastLevel] = useState(0);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            const loggedIn = await checkLoginStatus();
            setIsLoggedIn(loggedIn);

            if (loggedIn) {
                try {
                    const response = await fetchLevels();
                    const { totalLevels, lastLevel } = response.data;
                    const levelsData = Array.from({ length: totalLevels }, (_, i) => ({
                        index: i + 1,
                        unlocked: i <= lastLevel,
                        icon: `src/assets/level-icon-${i + 1}.png`,
                    }));
                    setLevels(levelsData);
                    setLastLevel(lastLevel);
                } catch (error) {
                    console.error('Error fetching levels:', error);
                }
            } else {
                navigate('/login');
            }
        };

        fetchData();

        return () => {
            saveGameState(); // Предположим, что эта функция сохраняет состояние игры
        };
    }, [navigate]);

    const handleLevelSelect = async (levelIndex: number) => {
        if (levelIndex <= lastLevel) {
            try {
                const response = await selectMap(levelIndex);
                setGameData(response.data);
                setIsLevelSelected(true);
            } catch (error) {
                console.error('Error selecting map:', error);
            }
        }
    };

    const handleLevelCompletion = () => {
        // Обработка завершения уровня
    };

    const handleGameCompletion = () => {
        navigate('/');
    };

    return (
        <div>
            <Header isLoggedIn={isLoggedIn} />
            {isLoggedIn ? (
                !isLevelSelected ? (
                    <LevelSelectionComponent
                        levels={levels}
                        onLevelSelect={handleLevelSelect}
                    />
                ) : (
                    <GameComponent
                        gameField={gameData}
                        setLevelCompleted={handleLevelCompletion}
                        setGameCompleted={handleGameCompletion}
                    />
                )
            ) : null}
            <Footer />
        </div>
    );
};

export default GameLevelPage;