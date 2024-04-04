import React, { createContext, useState } from 'react';

const PostContext = createContext({
    userPosts: [],
    followPosts: [],
    setUserPosts: () => {},
    setFollowPosts: () => {}
});

const PostProvider = ({ children }) => {
    const [userPosts, setUserPostsState] = useState([]);
    const [followPosts, setFollowPostsState] = useState([]);

    const setUserPosts = (posts) => {
        setUserPostsState(posts);
    };

    const setFollowPosts = (posts) => {
        setFollowPostsState(posts);
    };

    return (
        <PostContext.Provider value={{ userPosts, followPosts, setUserPosts, setFollowPosts }}>
            {children}
        </PostContext.Provider>
    );
};

export { PostContext, PostProvider };
