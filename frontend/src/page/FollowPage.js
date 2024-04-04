import React, {useContext, useEffect, useState} from "react";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import Header from "../components/Header";
import {useLocation} from "react-router-dom";
import {toast} from "../components/ui/use-toast";
import {AuthContext} from "../context/AuthContext";
import {useFollow} from "../context/FollowContext";
import PostList from "../components/PostList";

function FollowPage() {
    const location = useLocation();
    const userSearch = location.state?.userSearch;
    const { user, token } = useContext(AuthContext);
    const { followedIds, updateFollowedIds } = useFollow();
    const [userSearchPosts, setUserSearchPostsState] = useState([]);

    const handleFollow = () => {
        fetch(`/userfollow/follow`, {
            method: 'POST',
            body: JSON.stringify({ followerId: user.id, followedId: userSearch.id }),
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Unknown error');
                    });
                }
                return response.json();
            })
            .then(data => {
                updateFollowedIds(data)
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    };

    const handleUnfollow = () => {
        fetch(`/userfollow/unfollow`, {
            method: 'POST',
            body: JSON.stringify({ followerId: user.id, followedId: userSearch.id }),
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Unknown error');
                    });
                }
                return response.json();
            })
            .then(data => {
                updateFollowedIds(data)
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    };

    const getUserPosts = () => {
        fetch(`/post/getPosts/${userSearch.id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Unknown error');
                    });
                }
                return response.json();
            })
            .then(data => {
                setUserSearchPostsState(data);
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    }

    useEffect(() => {
        getUserPosts()
    }, []);

    const Body = () => {
        return (
            <div className="grid grid-cols-4">
                <div className="col-span-1">
                    <Left/>
                </div>
                <div className="col-span-2">
                    <Profile/>
                    <PostList posts={userSearchPosts}/>
                </div>
                <div className="col-span-1">
                    <Right/>
                </div>
            </div>
        );
    }

    const Profile = () => {
        if (!userSearch) {
            return <div className="bg-slate-100 m-6 p-6 text-gray-600">No results.</div>;
        }

        const isFollowed = followedIds.includes(userSearch.id);

        return (
            <div className="bg-slate-100 m-6 p-6">
                <div className="relative bg-cover bg-center bg-slate-200 h-64"
                     style={{ backgroundImage: `url(${userSearch.profile.bannerPicture || "/banner_picture_placeholder.png"})` }}>
                    <div className="absolute bottom-0 left-0 p-6">
                        <Avatar className="border-4 border-white rounded-full w-32 h-32">
                            <AvatarImage src={userSearch.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                    </div>
                </div>
                <div className="flex justify-between items-center pt-6">
                    <div className="flex flex-col">
                        <p className="text-2xl font-semibold">{userSearch.firstName && userSearch.lastName ? `${userSearch.firstName} ${userSearch.lastName}` : 'User Name'}</p>
                        <p className="text-gray-600">{userSearch.profile.biography || 'Bio.'}</p>
                    </div>
                    {isFollowed ? (
                        <button
                            onClick={handleUnfollow}
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                        >
                            Unfollow
                        </button>
                    ) : (
                        <button
                            onClick={handleFollow}
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                        >
                            Follow
                        </button>
                    )}
                </div>
            </div>
        );
    }

    const Left = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">Left</div>
        );
    }

    const Right = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">Right</div>
        );
    }

    return (
        <div>
            <Header/>
            <Body/>
        </div>
    );
}

export default FollowPage;
