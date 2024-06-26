import React, {useContext, useEffect, useState} from "react";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import {Button} from "../components/ui/button";
import {
    Carousel,
    CarouselContent,
    CarouselItem,
    CarouselNext,
    CarouselPrevious,
} from "../components/ui/carousel"
import {AuthContext} from "../context/AuthContext";
import {toast} from "../components/ui/use-toast";
import {PostContext} from "../context/PostContext";
import { Trash2 } from 'lucide-react';
import { ThumbsUp, ThumbsDown } from 'lucide-react';
import { MessageSquarePlus } from 'lucide-react';
import {useNavigate} from "react-router-dom";

function Post({ post }) {
    const { user, token } = useContext(AuthContext);
    const { setUserPosts } = useContext(PostContext);
    const [reactions, setReactions] = useState([]);
    const navigate = useNavigate();

    const handleCommentClick = () => {
        navigate('/post', { state: { post } });
    };

    const handleUserClick = () => {
        if (user && user.id === post.author.id) {
            navigate('/profile');
        } else {
            navigate('/follow', { state: { userSearch: post.author } });
        }
    };

    const handleDelete = () => {
        fetch(`/post/deletePost/${post.id}`, {
            method: 'DELETE',
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
                setUserPosts(data)
                navigate('/profile');
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    };

    const getPostReactions = () => {
        fetch(`/reaction/getReactions/${post.id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            }
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
                setReactions(data)
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    }

    const addReaction = (reactionType) => {
        const reactionDTO = {
            type: reactionType,
            author: user,
            post: post
        }

        fetch(`/reaction/addReaction`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reactionDTO),
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
                setReactions(data)
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
        getPostReactions()
    }, []);

    const countReactions = (type) => {
        return reactions.filter(reaction => reaction.type === type).length;
    };

    return (
        <div className="bg-slate-100 rounded-lg relative">
            <div className="p-4 flex justify-between items-center">
                <div className="flex items-center space-x-3 cursor-pointer" onClick={handleUserClick}>
                    <Avatar>
                        <AvatarImage src={post.author.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <div>
                        <p className="font-semibold">{post.author.firstName} {post.author.lastName}</p>
                        <p className="text-sm text-gray-600">{new Date(post.postDate).toLocaleString()}</p>
                    </div>
                </div>
                {user && user.id === post.author.id && (
                    <button
                        onClick={handleDelete}
                        className="px-4 py-2 text-white bg-gray-500 rounded-lg shadow-md hover:bg-gray-600 focus:outline-none focus:shadow-outline-gray active:bg-gray-600 cursor-pointer"
                    >
                        <Trash2 color="white"/>
                    </button>
                )}
            </div>
            <div className="p-4">
                <p>{post.content}</p>
                {post.images && post.images.length > 0 && (
                    <div className="mt-2">
                        <Carousel className="mx-auto w-4/5">
                            <CarouselContent>
                                {post.images.map((image, index) => (
                                    <CarouselItem key={index} className="flex justify-center items-center h-50">
                                        <img src={image} alt="Post" className="max-h-full max-w-full object-contain rounded-lg"/>
                                    </CarouselItem>
                                ))}
                            </CarouselContent>
                            <CarouselPrevious/>
                            <CarouselNext/>
                        </Carousel>
                    </div>
                )}
            </div>
            <div className="p-4 flex justify-between items-center">
                <div className="flex space-x-4">
                    <button onClick={() => addReaction('LIKE')}
                            className="px-4 py-2 text-white bg-blue-500 rounded-lg shadow-md hover:bg-blue-600 focus:outline-none focus:shadow-outline-blue active:bg-blue-600 cursor-pointer flex items-center">
                        <ThumbsUp color="white" className="mr-2"/> Like {countReactions('LIKE')}
                    </button>
                    <button onClick={() => addReaction('DISLIKE')}
                            className="px-4 py-2 text-white bg-red-500 rounded-lg shadow-md hover:bg-red-600 focus:outline-none focus:shadow-outline-red active:bg-red-600 cursor-pointer flex items-center">
                        <ThumbsDown color="white" className="mr-2"/> Dislike {countReactions('DISLIKE')}
                    </button>
                </div>
                <Button onClick={handleCommentClick}>
                    <MessageSquarePlus color="white"/> Comment
                </Button>
            </div>
        </div>
    );

}

export default Post;