import React, { useContext, useEffect, useState } from "react";
import { useLocation } from 'react-router-dom';
import Header from "../components/Header";
import { AuthContext } from "../context/AuthContext";
import { toast } from "../components/ui/use-toast";
import { Textarea } from "../components/ui/textarea";
import { Button } from "../components/ui/button";

function MessagePage() {
    const { user, token } = useContext(AuthContext);
    const location = useLocation();
    const [selectedConversation, setSelectedConversation] = useState(null);
    const [conversations, setConversations] = useState([]);
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        if (location.state && location.state.selectedConversation) {
            setSelectedConversation(location.state.selectedConversation);
            getMessages(location.state.selectedConversation.id);
        }
    }, [location.state]);

    const getConversations = () => {
        fetch(`/message/getConversations/${user.id}`, {
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
                setConversations(data);
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                });
            });
    };

    const getMessages = (conversationId) => {
        fetch(`/message/getMessages/${conversationId}`, {
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
                setMessages(data);
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
        getConversations();
    }, []);

    const handleSelectConversation = (conversation) => {
        setSelectedConversation(conversation);
        getMessages(conversation.id);
    };

    const ConversationList = () => (
        <div className="w-1/4 max-h-screen bg-slate-100 overflow-y-auto">
            {conversations.map(conversation => (
                <div
                    key={conversation.id}
                    onClick={() => handleSelectConversation(conversation)}
                    className={`p-4 hover:bg-slate-200 ${selectedConversation && selectedConversation.id === conversation.id ? 'bg-blue-500' : ''}`}
                >
                    {conversation.participants.map(p => p.firstName).join(", ")}
                </div>
            ))}
        </div>
    );

    const TextAreaWithButton = () => {
        const [message, setMessage] = useState("");

        const sendMessage = () => {
            const messageDTO = {
                content: message,
                author: user,
                conversation: selectedConversation
            }

            fetch(`/message/sendMessage`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(messageDTO),
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
                    setMessage("");
                    setMessages(data)
                })
                .catch(error => {
                    toast({
                        variant: "destructive",
                        title: "Uh oh! Something went wrong.",
                        description: error.message,
                    });
                });
        };

        return (
            <div className="grid w-full gap-2">
                <Textarea
                    placeholder="Type your message here."
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                />
                <Button onClick={sendMessage}>Send message</Button>
            </div>
        );
    }

    const Body = () => (
        <div className="flex m-6 flex-1 overflow-hidden">
            <ConversationList />
            <div className="flex flex-col flex-1 bg-slate-100 p-6">
                {selectedConversation ? (
                    <div className="flex flex-col h-full">
                        <div className="flex-1 overflow-y-auto mb-4" style={{ maxHeight: '80vh' }}>
                            {messages.map(message => (
                                <div
                                    key={message.id}
                                    className={`p-4 my-2 rounded-lg max-w-3/4 ${
                                        message.author.id === user.id
                                            ? 'ml-auto bg-blue-100'
                                            : 'mr-auto bg-white'
                                    }`}
                                >
                                    {message.content}
                                </div>
                            ))}
                        </div>
                        <TextAreaWithButton />
                    </div>
                ) : (
                    <div>Select a conversation to view messages.</div>
                )}
            </div>
        </div>
    );

    return (
        <div className="flex flex-col min-h-screen h-screen">
            <Header />
            <Body />
        </div>
    );
}

export default MessagePage;
