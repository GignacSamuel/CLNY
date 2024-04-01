import React, {useContext, useRef} from "react";
import {Button} from "../components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar"
import {AuthContext} from "../context/AuthContext";
import { Pencil } from 'lucide-react';
import { toast } from '../components/ui/use-toast';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "../components/ui/dialog"
import { Textarea } from "../components/ui/textarea"
import Header from "../components/Header";
import NewPost from "../components/NewPost";

function ProfilePage() {
    const { user, token, setUser } = useContext(AuthContext);
    const bioRef = useRef(null);

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

        if (!file) {
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        fetch(`/user/updateBannerPicture/${user.id}`, {
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

    const handleBiographyChange = () => {
        const newBio = bioRef.current.value;

        fetch(`/user/updateBiography/${user.id}`, {
            method: 'PUT',
            body: JSON.stringify({ newBiography: newBio }),
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
                setUser(data);
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                })
            });
    }

    const Body = () => {
        return (
            <div className="grid grid-cols-4">
                <div className="col-span-1">
                    <Left/>
                </div>
                <div className="col-span-2">
                    <Profile/>
                    <NewPost/>
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
                <div className="relative bg-cover bg-center bg-slate-200 h-64"
                     style={{backgroundImage: `url(${user.profile.bannerPicture || "/banner_picture_placeholder.png"})`,}}>
                    <input id="banner-pic" type="file" accept="image/*" onChange={handleBannerPicChange}
                           className="hidden"/>
                    <label htmlFor="banner-pic" className="absolute bottom-0 right-0 m-4 cursor-pointer">
                        <div
                            className="p-2 bg-black bg-opacity-50 rounded-full hover:bg-opacity-75 transition-opacity duration-300">
                            <Pencil color="white"/>
                        </div>
                    </label>
                    <div className="absolute bottom-0 left-0 p-6">
                        <div className="relative group">
                            <Avatar className="border-4 border-white rounded-full w-32 h-32">
                                <AvatarImage src={user.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                                <AvatarFallback>CN</AvatarFallback>
                            </Avatar>
                            <label htmlFor="profile-pic"
                                   className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 rounded-full cursor-pointer opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                                <Pencil color="white"/>
                                <input id="profile-pic" type="file" accept="image/*" onChange={handleProfilePicChange}
                                       className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"/>
                            </label>
                        </div>
                    </div>
                </div>
                <div className="pt-6 flex flex-col">
                    <p className="text-2xl font-semibold">{user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : 'User Name'}</p>
                    <div className="flex justify-between items-center">
                        <p className="text-gray-600">{user.profile.biography || 'Bio.'}</p>
                        <Dialog>
                            <DialogTrigger asChild>
                                <Button>Edit Bio</Button>
                            </DialogTrigger>
                            <DialogContent>
                                <DialogHeader>
                                    <DialogTitle>Edit Biography</DialogTitle>
                                    <DialogDescription>
                                        Update your biography information below.
                                    </DialogDescription>
                                </DialogHeader>
                                <Textarea
                                    ref={bioRef}
                                    defaultValue={user.profile.biography || 'Bio.'}
                                    className="w-full mt-4"
                                />
                                <Button onClick={handleBiographyChange}>Save changes</Button>
                            </DialogContent>
                        </Dialog>
                    </div>
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