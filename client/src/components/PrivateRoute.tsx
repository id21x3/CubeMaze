// PrivateRoute.tsx
import React from 'react';
import { Navigate } from 'react-router-dom';
import { checkLoginStatus } from '../services/authService';

interface PrivateRouteProps {
    children: React.ReactElement;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ children }) => {
    const isLoggedIn = checkLoginStatus();

    if (!isLoggedIn) {
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default PrivateRoute;