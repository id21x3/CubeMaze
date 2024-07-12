import apiClient from '../utils/apiClient';

interface LoginCredentials {
    username: string;
    password: string;
}

interface SignupCredentials extends LoginCredentials {
    confirmPassword: string;
}

const signIn = async (credentials: LoginCredentials) => {
    try {
        const response = await apiClient.post('/auth/signin', credentials);
        localStorage.setItem('token', response.data);
        return true;
    } catch (error) {
        console.error('Ошибка авторизации:', error);
        return false;
    }
};

const signUp = async (credentials: SignupCredentials) => {
    try {
        await apiClient.post('/auth/signup', credentials);
        return true;
    } catch (error) {
        console.error('Ошибка регистрации:', error);
        return false;
    }
};

const checkLoginStatus = async () => {
    const token = localStorage.getItem('token');
    return !!token;
};

const logout = () => {
    localStorage.removeItem('token');
};

export { signIn, signUp, checkLoginStatus, logout };
