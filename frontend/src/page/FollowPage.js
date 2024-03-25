import React from "react";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import Header from "../components/Header";
import {useLocation} from "react-router-dom";

function FollowPage() {
    const location = useLocation();
    const user = location.state?.user;

    const handleFollow = () => {
        console.log('Follow button clicked');
    };

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
        if (!user) {
            return <div className="bg-slate-100 m-6 p-6 text-gray-600">No results.</div>;
        }

        return (
            <div className="bg-slate-100 m-6 p-6">
                <div className="relative bg-cover bg-center bg-slate-200 h-64"
                     style={{ backgroundImage: `url(${user.profile.bannerPicture || "/banner_picture_placeholder.png"})` }}>
                    <div className="absolute bottom-0 left-0 p-6">
                        <Avatar className="border-4 border-white rounded-full w-32 h-32">
                            <AvatarImage src={user.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                    </div>
                </div>
                <div className="flex justify-between items-center pt-6">
                    <div className="flex flex-col">
                        <p className="text-2xl font-semibold">{user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : 'User Name'}</p>
                        <p className="text-gray-600">{user.profile.biography || 'Bio.'}</p>
                    </div>
                    <button
                        onClick={handleFollow}
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                    >
                        Follow
                    </button>
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

export default FollowPage;
