import React, { useContext, useEffect, useState } from "react";
import { useLocation } from 'react-router-dom';
import Header from "../components/Header";
import { AuthContext } from "../context/AuthContext";
import { toast } from "../components/ui/use-toast";

function MessagePage() {
    const { user, token } = useContext(AuthContext);
    const location = useLocation();
    const [selectedConversation, setSelectedConversation] = useState(null);
    const [conversations, setConversations] = useState([]);

    useEffect(() => {
        if (location.state && location.state.selectedConversation) {
            setSelectedConversation(location.state.selectedConversation);
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

    useEffect(() => {
        getConversations();
    }, []);

    const handleSelectConversation = (conversation) => {
        setSelectedConversation(conversation);
    };

    const ConversationList = () => (
        <div className="w-1/4 bg-slate-100 overflow-y-auto">
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

    const Body = () => (
        <div className="flex m-6 flex-1">
            <ConversationList />
            <div className="flex-1 bg-slate-100 p-6">
                {selectedConversation ? (
                    <div>Selected Conversation: {selectedConversation.id}</div>
                ) : (
                    <div>Select a conversation.</div>
                )}
            </div>
        </div>
    );

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <Body />
        </div>
    );
}

export default MessagePage;
