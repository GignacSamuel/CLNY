import Header from "../components/Header";
import Post from "../components/Post";
import React from "react";
import {useLocation} from "react-router-dom";
import CommentsSection from "../components/CommentSection";

function PostPage() {
    const location = useLocation();
    const post = location.state.post;

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
            <div className="bg-slate-100 m-6 p-6">Left</div>
        );
    }

    const Middle = () => {
        if (!post) {
            return <div className="bg-slate-100 m-6 p-6">No results</div>;
        }

        return (
            <div className="bg-slate-100 m-6 p-6">
                <Post key={post.id} post={post}/>
                <CommentsSection post={post}/>
            </div>
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

export default PostPage;