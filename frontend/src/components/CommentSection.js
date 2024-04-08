import React, { useState } from "react";
import { Button } from "../components/ui/button";
import { Send, Reply } from "lucide-react";

function CommentsSection() {
    const [comment, setComment] = useState("");
    const [replies, setReplies] = useState({});
    const [activeReplyBox, setActiveReplyBox] = useState(null);

    const handleCommentSubmit = (e) => {
        e.preventDefault();
        console.log("Comment submitted: ", comment);
        setComment("");
    };

    const handleReplySubmit = (replyContent, parentId) => {
        console.log("Reply submitted for comment id", parentId, "with content:", replyContent);
        setReplies({ ...replies, [parentId]: replyContent });
        setActiveReplyBox(null);
    };

    const Comment = ({ id, content, children }) => {
        const [reply, setReply] = useState("");

        return (
            <div className="bg-slate-200 p-4 rounded-lg mt-4">
                <p>{content}</p>
                <div className="flex justify-start mt-2">
                    <Button onClick={() => setActiveReplyBox(id === activeReplyBox ? null : id)}>
                        <Reply color="white"/> Reply
                    </Button>
                </div>
                {activeReplyBox === id && (
                    <div className="mt-2">
                        <textarea
                            className="w-full p-2 border rounded-lg"
                            placeholder="Write a reply..."
                            value={reply}
                            onChange={(e) => setReply(e.target.value)}
                        />
                        <Button onClick={() => handleReplySubmit(reply, id)} className="mt-2">
                            <Send color="white" className="mr-2"/> Send
                        </Button>
                    </div>
                )}
                {children}
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
                {/* Placeholder comments */}
                <Comment id="1" content="This is a comment">
                    {/* Display reply if exists */}
                    {replies['1'] && <Comment id="1-1" content={replies['1']} />}
                </Comment>
                <Comment id="2" content="This is another comment">
                    {replies['2'] && <Comment id="2-1" content={replies['2']} />}
                </Comment>
            </div>
        </div>
    );
}

export default CommentsSection;
