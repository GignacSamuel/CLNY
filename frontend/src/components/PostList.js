import React from "react";
import Post from "./Post";

function PostList({ posts }) {

    const sortedPosts = posts.sort((a, b) => new Date(b.postDate) - new Date(a.postDate));

    return (
        <div className="space-y-4 p-6">
            {sortedPosts.map(post => (
                <Post key={post.id} post={post}/>
            ))}
        </div>
    );
}

export default PostList;
