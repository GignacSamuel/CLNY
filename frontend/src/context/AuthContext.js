import React, { createContext, useState } from 'react';

const AuthContext = createContext();

const AuthContextProvider = ({ children }) => {
    const [authState, setAuthState] = useState({
        user: null,
        token: null,
    });

    const setUser = (user) => {
        setAuthState((prevState) => ({ ...prevState, user }));
    };

    const setToken = (token) => {
        setAuthState((prevState) => ({ ...prevState, token }));
    };

    return (
        <AuthContext.Provider value={{ ...authState, setUser, setToken }}>
            {children}
        </AuthContext.Provider>
    );
};

export { AuthContext, AuthContextProvider };
