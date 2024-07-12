// App.tsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import InfoPage from './pages/InfoPage';
import GameLevelPage from './pages/GameLevelPage';
import PrivateRoute from './components/PrivateRoute';

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route
                    path="/info"
                    element={
                        <PrivateRoute>
                            <InfoPage />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/game"
                    element={
                        <PrivateRoute>
                            <GameLevelPage />
                        </PrivateRoute>
                    }
                />
            </Routes>
        </Router>
    );
};

export default App;