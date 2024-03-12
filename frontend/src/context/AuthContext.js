import React, { createContext, useState, useEffect } from 'react';

const AuthContext = createContext();

const getInitialState = () => {
    const storedUser = sessionStorage.getItem('user');
    const storedToken = sessionStorage.getItem('token');
    return {
        user: storedUser ? JSON.parse(storedUser) : null,
        token: storedToken || null,
    };
};

const AuthContextProvider = ({ children }) => {
    const [authState, setAuthState] = useState(getInitialState);

    const setUser = (user) => {
        setAuthState((prevState) => ({ ...prevState, user }));
        sessionStorage.setItem('user', JSON.stringify(user));
    };

    const setToken = (token) => {
        setAuthState((prevState) => ({ ...prevState, token }));
        sessionStorage.setItem('token', token);
    };

    const logout = () => {
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('token');
        setAuthState({
            user: null,
            token: null,
        });
    };

    useEffect(() => {}, []);

    return (
        <AuthContext.Provider value={{ ...authState, setUser, setToken, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export { AuthContext, AuthContextProvider };
