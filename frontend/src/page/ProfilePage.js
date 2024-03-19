import React, {useContext} from "react";
import {Input} from "../components/ui/input";
import {Button} from "../components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar"
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";
import { Pencil } from 'lucide-react';
import { useToast } from "../components/ui/use-toast"

function ProfilePage() {
    const navigate = useNavigate();
    const { logout, user, token, setUser } = useContext(AuthContext);
    const { toast } = useToast()

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const handleProfileClick = () => {
        navigate('/profile');
    };

    const handleProfilePicChange = (event) => {
        const file = event.target.files[0];

        if (!file) {
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        fetch(`/user/updateProfilePicture/${user.id}`, {
            method: 'PUT',
            body: formData,
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
                setUser(data);
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    };

    const handleBannerPicChange = (event) => {
        const file = event.target.files[0];
        console.log(file);
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
                            <AvatarImage src={user.profile.profilePicture || "https://github.com/shadcn.png"}/>
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
                <div className="relative bg-cover bg-center bg-slate-200 h-64">
                    <input id="banner-pic" type="file" accept="image/*" onChange={handleBannerPicChange} className="hidden" />
                    <label htmlFor="banner-pic" className="absolute bottom-0 right-0 m-4 cursor-pointer">
                        <div className="p-2 bg-black bg-opacity-50 rounded-full hover:bg-opacity-75 transition-opacity duration-300">
                            <Pencil color="white" />
                        </div>
                    </label>
                    <div className="absolute bottom-0 left-0 p-6">
                        <div className="relative group">
                            <Avatar className="border-4 border-white rounded-full w-32 h-32">
                                <AvatarImage src={user.profile.profilePicture || "https://github.com/shadcn.png"}/>
                                <AvatarFallback>CN</AvatarFallback>
                            </Avatar>
                            <label htmlFor="profile-pic" className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 rounded-full cursor-pointer opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                                <Pencil color="white" />
                                <input id="profile-pic" type="file" accept="image/*" onChange={handleProfilePicChange} className="absolute inset-0 w-full h-full opacity-0 cursor-pointer" />
                            </label>
                        </div>
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