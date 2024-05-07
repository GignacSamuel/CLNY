import Header from "../components/Header";
import NewPost from "../components/NewPost"
import PostList from "../components/PostList";
import React, {useContext, useEffect, useState} from "react";
import {AuthContext} from "../context/AuthContext";
import {toast} from "../components/ui/use-toast";
import CalendarWidget from "../components/widgets/CalendarWidget";
import WeatherWidget from "../components/widgets/WeatherWidget";

function HomePage() {
    const [feedPosts, setFeedPosts] = useState([]);
    const { user, token } = useContext(AuthContext);

    const getFeedPosts = () => {
        fetch(`/post/getFeed/${user.id}`, {
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
                setFeedPosts(data)
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                });
            });
    }

    useEffect(() => {
        getFeedPosts()
    }, []);

    const Body = () => {
        return (
            <div className="grid grid-cols-4">
                <div className="col-span-1">
                    <Left/>
                </div>
                <div className="col-span-2">
                    <Middle/>
                </div>
                <div className="col-span-1">
                    <Right/>
                </div>
            </div>
        );
    }

    const Left = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <CalendarWidget/>
            </div>
        );
    }

    const Middle = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <NewPost />
                <PostList posts={feedPosts}/>
            </div>
        );
    }

    const Right = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <WeatherWidget/>
            </div>
        );
    }

    return (
        <div>
            <Header/>
            <Body/>
        </div>
    );
}

export default HomePage;