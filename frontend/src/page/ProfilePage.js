import React, {useContext} from "react";
import {Input} from "../components/ui/input";
import {Button} from "../components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar"
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";

function ProfilePage() {
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
                            <AvatarImage src="https://github.com/shadcn.png"/>
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
            <div className="grid grid-cols-4">
                <div className="col-span-1">
                    <Left/>
                </div>
                <div className="col-span-2">
                    <Profile/>
                </div>
                <div className="col-span-1">
                    <Right/>
                </div>
            </div>
        );
    }

    const Profile = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <div className="bg-cover bg-center bg-slate-200 h-64">
                    <div className="relative h-full flex items-end justify-start p-6">
                        <Avatar className="border-4 border-white rounded-full w-32 h-32">
                            <AvatarImage src="https://github.com/shadcn.png"/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                    </div>
                </div>
                <div className="pt-6">
                    <p className="text-2xl font-semibold">{user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : 'User Name'}</p>
                    <p className="text-gray-600">{user.profile.biography ? `${user.profile.biography}` : 'Bio.'}</p>
                </div>
            </div>
        );
    }

    const Left = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">Left</div>
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

export default ProfilePage;