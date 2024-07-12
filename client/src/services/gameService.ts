// services/gameService.ts
import apiClient  from '../utils/apiClient';

export const fetchLevels = async () => {
    try {
        return await apiClient.get('/secured/cubemaze');
    } catch (error) {
        throw new Error('Error fetching levels');
    }
};

export const selectMap = async (levelIndex: number) => {
    try {
        return await apiClient.post('/secured/cubemaze/selectMap', { levelIndex });
    } catch (error) {
        throw new Error('Error selecting map');
    }
};

export const moveCube = async (direction: string) => {
    try {
        return await apiClient.post('/secured/cubemaze/move', { direction });
    } catch (error) {
        throw new Error('Error moving cube');
    }
};

export const getAverageRating = async (): Promise<number> => {
    const response = await apiClient.get('/secured/api/rating/CubeMaze');
    return response.data;
};

export const getPlayerInfo = async (): Promise<{
    name: string;
    maxScore: number;
    rating: number;
    lastComment: string;
}> => {
    const [scoreResponse, ratingResponse, commentResponse] = await Promise.all([
        apiClient.get('/secured/api/score/maxPoints'), // Запрос информации о текущем пользователе
        apiClient.get('/secured/api/rating/CubeMaze'), // Запрос рейтинга для текущего пользователя
        apiClient.get('/secured/api/comment/lastComment'), // Запрос последнего комментария для текущего пользователя
    ]);

    const maxScore = scoreResponse.data.points;
    const rating = ratingResponse.data;
    const lastComment = commentResponse.data?.comment || '';

    return {
        name: scoreResponse.data.player,
        maxScore,
        rating,
        lastComment,
    };
};

interface TopPlayer {
    name: string;
    score: number;
    lastComment: string;
}

export const getTopPlayers = async (): Promise<TopPlayer[]> => {
    const [scoreResponse, commentResponse] = await Promise.all([
        apiClient.get('/secured/api/score/CubeMaze'),
        apiClient.get('/secured/api/comment/CubeMaze'),
    ]);

    const players: TopPlayer[] = [];

    for (let i = 0; i < scoreResponse.data.length && i < 10; i++) {
        const score = scoreResponse.data[i];
        const comment = commentResponse.data.find((c: { player: string }) => c.player === score.player)?.comment || '';

        players.push({
            name: score.player,
            score: score.points,
            lastComment: comment,
        });
    }

    return players;
};

export const addComment = async (comment: string): Promise<void> => {
    await apiClient.post('/secured/api/comment', {
        game: 'CubeMaze',
        comment: comment,
        commentedOn: new Date().toISOString(),
    });
};

export const rateGame = async (rating: number): Promise<void> => {
    await apiClient.post('/secured/api/rating', {
        game: 'CubeMaze',
        rating: rating,
        ratedOn: new Date().toISOString(),
    });
};

export const saveGameState = async (): Promise<void> => {
    try {
        await apiClient.post('/secured/cubemaze/save');
    } catch (error) {
        throw new Error('Error saving game state');
    }
};