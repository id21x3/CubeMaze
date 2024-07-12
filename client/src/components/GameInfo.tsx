import React, { useEffect, useState } from 'react';
import { getAverageRating, getPlayerInfo, getTopPlayers, addComment, rateGame } from '../services/gameService';
import styles from '../styles/InfoPage.module.css';

interface PlayerInfo {
    name: string;
    maxScore: number;
    rating: number;
    lastComment: string;
}

interface TopPlayer {
    name: string;
    score: number;
    lastComment: string;
}

const GameInfo: React.FC = () => {
    const [averageRating, setAverageRating] = useState<number>(0);
    const [playerInfo, setPlayerInfo] = useState<PlayerInfo | null>(null);
    const [topPlayers, setTopPlayers] = useState<TopPlayer[]>([]);
    const [newComment, setNewComment] = useState<string>('');
    const [newRating, setNewRating] = useState<number>(0);

    useEffect(() => {
        const fetchData = async () => {
            const averageRating = await getAverageRating();
            const playerInfo = await getPlayerInfo();
            const topPlayers = await getTopPlayers();

            setAverageRating(averageRating);
            setPlayerInfo(playerInfo);
            setTopPlayers(topPlayers);
        };

        fetchData();
    }, []);

    const handleCommentSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await addComment(newComment);
        setNewComment('');
        // Обновить список комментариев
    };

    const handleRatingSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await rateGame(newRating);
        setNewRating(0);
        // Обновить рейтинг игры
    };

    return (
        <div className={styles.pageInfoContainer}>
            <div className={styles.infoContainer}>
                <h2>Game Rating: {averageRating}</h2>

                {playerInfo && (
                    <div className={styles.playerInfo}>
                        <h3>You</h3>
                        <h4>{playerInfo.name}({playerInfo.maxScore}) rating: {playerInfo.rating} "{playerInfo.lastComment}"</h4>
                    </div>
                )}
            </div>

            <div className={styles.topPlayersContainer}>
                <h3>Top Players</h3>
                <ul className={styles.topPlayersList}>
                    {topPlayers.map((player, index) => (
                        <li key={index} className={styles.topPlayerItem}>
                            <p>{player.name}({player.score}) "{player.lastComment}"</p>
                        </li>
                    ))}
                </ul>
            </div>

            <div className={styles.formContainer}>
                <form onSubmit={handleCommentSubmit} className={styles.commentForm}>
                    <h3>Add Comment</h3>
                    <textarea
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                        placeholder="Enter your comment"
                        className={styles.textarea}
                    ></textarea>
                    <button type="submit" className={styles.button}>
                        Submit Comment
                    </button>
                </form>

                <form onSubmit={handleRatingSubmit} className={styles.ratingForm}>
                    <h3>Rate Game</h3>
                    <input
                        type="number"
                        min="1"
                        max="5"
                        value={newRating}
                        onChange={(e) => setNewRating(parseInt(e.target.value))}
                        className={styles.input}
                    />
                    <button type="submit" className={styles.button}>
                        Submit Rating
                    </button>
                </form>
            </div>
        </div>
    );
};

export default GameInfo;