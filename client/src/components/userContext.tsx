// userContext.tsx
import React, { createContext, useContext, useState, ReactNode } from 'react';

interface User {
    username: string;
    loggedIn: boolean;
}

interface UserContextType {
    user: User | null;
    login: (username: string) => void;
    logout: () => void;
}

const UserContext = createContext<UserContextType>({
    user: null,
    login: () => {},
    logout: () => {},
});

export const useUser = () => useContext(UserContext);

interface UserProviderProps {
    children: ReactNode;
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);

    const login = (username: string) => {
        setUser({ username, loggedIn: true });
    };

    const logout = () => {
        setUser(null);
    };

    return (
        <UserContext.Provider value={{ user, login, logout }}>
            {children}
        </UserContext.Provider>
    );
};
