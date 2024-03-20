import React, {useContext} from "react";
import {Input} from "../components/ui/input";
import {Button} from "../components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar"
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";

function HomePage() {
    const navigate = useNavigate();
    const { logout, user } = useContext(AuthContext);

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const handleProfileClick = () => {
        navigate('/profile');
    };

    const Header = () => {
        return (
            <nav className="flex items-center justify-between flex-wrap bg-slate-100 p-6">
                <div className="flex items-center flex-shrink-0 mr-6">
                    <img className="rounded-lg h-[50px]" src="/logo_small.png" alt="CLNY Logo"/>
                </div>
                <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
                    <div className="lg:flex-grow">
                        <div className="flex w-full max-w-sm items-center space-x-2">
                            <Input placeholder="Search"/>
                            <Button type="submit">Search</Button>
                        </div>
                    </div>
                    <div className="mr-2 cursor-pointer" onClick={handleProfileClick}>
                        <Avatar>
                            <AvatarImage src={user.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                    </div>
                    <div>
                        <Button variant="destructive" onClick={handleLogout}>Logout</Button>
                    </div>
                </div>
            </nav>
        );
    }

    const Body = () => {
        return (
            <div className="grid grid-cols-4 gap-4">
                <div className="col-span-1">left</div>
                <div className="col-span-2">middle</div>
                <div className="col-span-1">right</div>
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