import React, { createContext, useState, useContext } from 'react';

const FollowContext = createContext();

const FollowProvider = ({ children }) => {
    const [followedIds, setFollowedIds] = useState([]);
    const [followerIds, setFollowerIds] = useState([]);

    const updateFollowedIds = (ids) => {
        console.log(ids)
        setFollowedIds(ids);
    };

    const updateFollowerIds = (ids) => {
        setFollowerIds(ids);
    };

    return (
        <FollowContext.Provider value={{
            followedIds,
            followerIds,
            updateFollowedIds,
            updateFollowerIds
        }}>
            {children}
        </FollowContext.Provider>
    );
};

const useFollow = () => useContext(FollowContext);

export { FollowProvider, useFollow };
