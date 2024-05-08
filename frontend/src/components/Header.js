import {Input} from "../components/ui/input";
import {Button} from "../components/ui/button";
import {Avatar, AvatarFallback, AvatarImage} from "../components/ui/avatar";
import React, {useContext, useRef} from "react";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";
import { toast } from '../components/ui/use-toast';
import { LogOut, Home, MailOpen } from 'lucide-react';

function Header() {
    const navigate = useNavigate();
    const { logout, user, token } = useContext(AuthContext);
    const searchRef = useRef(null);

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const handleProfileClick = () => {
        navigate('/profile');
    };

    const handleHomeClick = () => {
        navigate('/home');
    };

    const handleMessagesClick = () => {
        navigate('/message');
    };

    const handleUserSearch = () => {
        const searchString = searchRef.current.value;

        fetch(`/user/search`, {
            method: 'POST',
            body: JSON.stringify({ searchString: searchString }),
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
                navigate('/search', { state: { searchResults: data } });
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    };

    return (
        <nav className="flex items-center justify-between flex-wrap bg-slate-100 p-6">
            <div className="flex items-center flex-shrink-0 mr-6">
                <img className="rounded-lg h-[50px]" src="/logo_small.png" alt="CLNY Logo"/>
            </div>
            <div className="w-full block flex-grow lg:flex md:flex items-center lg:w-auto md:w-auto">
                <Button onClick={handleHomeClick} className="mr-4">
                    <Home color="white"/>
                </Button>
                <div className="flex-grow">
                    <div className="flex w-full max-w-sm items-center space-x-2">
                        <Input
                            placeholder="Rechercher un utilisateur"
                            ref={searchRef}
                        />
                        <Button type="button" onClick={handleUserSearch}>Rechercher</Button>
                    </div>
                </div>
                <Button onClick={handleMessagesClick} className="bg-green-500 hover:bg-green-600 mr-2">
                    <MailOpen color="white"/>
                </Button>
                <div className="mr-2 cursor-pointer" onClick={handleProfileClick}>
                    <Avatar>
                        <AvatarImage src={user.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                </div>
                <div>
                    <Button variant="destructive" onClick={handleLogout}>
                        <LogOut color="white"/> Se déconnecter
                    </Button>
                </div>
            </div>
        </nav>
    );

}

export default Header;