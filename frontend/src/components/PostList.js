import React from "react";
import {Avatar, AvatarFallback, AvatarImage} from "../components/ui/avatar";

function PostList({ posts }) {

    const Post = ({ post }) => {
        return (
            <div className="border p-4 rounded-lg shadow-lg space-y-2">
                <div className="flex space-x-2">
                    <div>
                        <Avatar>
                            <AvatarImage src={post.author.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                    </div>
                    <div>
                        <p className="font-semibold">{post.author.firstName} {post.author.lastName}</p>
                        <p className="text-sm text-gray-500">{new Date(post.postDate).toLocaleString()}</p>
                    </div>
                </div>
                <div>
                    <p>{post.content}</p>
                    {post.images.map((image, index) => (
                        <img key={index} src={image} alt="Post" className="mt-2" />
                    ))}
                </div>
                <div className="flex space-x-4">
                    <button className="px-4 py-2 rounded text-white bg-blue-500 hover:bg-blue-600">Like</button>
                    <button className="px-4 py-2 rounded text-white bg-red-500 hover:bg-red-600">Dislike</button>
                    <button className="px-4 py-2 rounded text-white bg-gray-500 hover:bg-gray-600">Comment</button>
                </div>
            </div>
        );
    };

    return (
        <div className="space-y-4">
            {posts.map(post => (
                <Post key={post.id} post={post} />
            ))}
        </div>
    );

}

export default PostList;