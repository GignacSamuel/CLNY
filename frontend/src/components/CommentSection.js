import React, { useContext, useEffect, useState } from 'react';
import { Button } from '../components/ui/button';
import { Send, Reply } from 'lucide-react';
import { toast } from "../components/ui/use-toast";
import { AuthContext } from "../context/AuthContext";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";

function CommentsSection({ post }) {
    const [comment, setComment] = useState('');
    const [comments, setComments] = useState([]);
    const [activeReplyBox, setActiveReplyBox] = useState(null);
    const { user, token } = useContext(AuthContext);

    const handleCommentSubmit = (e) => {
        e.preventDefault();

        const commentDTO = {
            content: comment,
            author: user,
            post: post
        };

        submitComment(commentDTO);
    };

    const handleReplySubmit = (replyContent, parentId) => {
        const commentDTO = {
            content: replyContent,
            author: user,
            post: post,
            parentComment: { id: parentId }
        };

        submitComment(commentDTO);
        setActiveReplyBox(null);
    };

    const submitComment = (commentDTO) => {
        fetch(`/comment/createComment`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(commentDTO),
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
                setComment("");
                setComments(data)
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                });
            });
    };

    const getPostComments = () => {
        fetch(`/comment/getComments/${post.id}`, {
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
                setComments(data);
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                });
            });
    };

    useEffect(() => {
        getPostComments();
    }, [post.id]);

    const Comment = ({ node }) => {
        const [reply, setReply] = useState("");
        const { comment, replies } = node;

        return (
            <div className="bg-slate-200 p-4 rounded-lg mt-4">
                <div className="flex items-center space-x-3">
                    <Avatar>
                        <AvatarImage src={comment.author.profile.profilePicture || "/profile_picture_placeholder.jpg"} />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <div>
                        <p className="font-semibold">{comment.author.firstName} {comment.author.lastName}</p>
                        <p className="text-sm text-gray-600">{new Date(comment.commentDate).toLocaleString()}</p>
                    </div>
                </div>
                <p className="mt-4">{comment.content}</p>
                <div className="flex justify-start mt-2">
                    <Button onClick={() => setActiveReplyBox(comment.id === activeReplyBox ? null : comment.id)}>
                        <Reply color="white"/> Reply
                    </Button>
                </div>
                {activeReplyBox === comment.id && (
                    <div className="mt-2">
                        <textarea
                            className="w-full p-2 border rounded-lg"
                            placeholder="Write a reply..."
                            value={reply}
                            onChange={(e) => setReply(e.target.value)}
                        />
                        <Button onClick={() => handleReplySubmit(reply, comment.id)} className="mt-2">
                            <Send color="white" className="mr-2"/> Send
                        </Button>
                    </div>
                )}
                {replies && replies.length > 0 && (
                    <div className="ml-4">
                        {replies.map((replyNode) => (
                            <Comment key={replyNode.comment.id} node={replyNode} />
                        ))}
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className="my-6 mx-6">
            <form onSubmit={handleCommentSubmit} className="flex flex-col mb-6">
                <textarea
                    className="w-full p-2 border rounded-lg"
                    placeholder="Add a comment..."
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                />
                <button
                    type="submit"
                    className="self-end mt-2 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded flex items-center"
                >
                    <Send color="white" className="mr-2"/> Publish
                </button>
            </form>
            <div>
                {comments.map(node => (
                    <Comment key={node.comment.id} node={node} />
                ))}
            </div>
        </div>
    );
}

export default CommentsSection;
