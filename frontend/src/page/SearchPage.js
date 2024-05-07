import Header from "../components/Header";
import {useLocation, useNavigate} from "react-router-dom";
import React, {useContext} from "react";
import {AuthContext} from "../context/AuthContext";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar"
import CalendarWidget from "../components/widgets/CalendarWidget";
import WeatherWidget from "../components/widgets/WeatherWidget";

function SearchPage() {
    const location = useLocation();
    const searchResults = location.state?.searchResults;
    const navigate = useNavigate();
    const { user } = useContext(AuthContext);

    const Body = () => {
        return (
            <div className="grid grid-cols-4">
                <div className="col-span-1">
                    <Left/>
                </div>
                <div className="col-span-2">
                    <Search/>
                </div>
                <div className="col-span-1">
                    <Right/>
                </div>
            </div>
        );
    }

    const Left = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <CalendarWidget/>
            </div>
        );
    }

    const Search = () => {
        if (!searchResults || searchResults.length === 0) {
            return <div className="bg-slate-100 m-6 p-6 text-gray-600">No results.</div>;
        }

        return (
            <div className="bg-slate-100 m-6 p-6">
                {searchResults.map(result => (
                    <div key={result.id} className="bg-white p-4 mb-4 cursor-pointer" onClick={() => {
                        if (result.id === user.id) {
                            navigate('/profile');
                        } else {
                            navigate('/follow', { state: { userSearch: result } });
                        }
                    }}>
                        <div className="flex items-center space-x-4">
                            <Avatar className="border-4 border-white rounded-full w-16 h-16">
                                <AvatarImage src={result.profile.profilePicture || "/profile_picture_placeholder.jpg"}/>
                                <AvatarFallback>CN</AvatarFallback>
                            </Avatar>
                            <div className="flex-1">
                                <p className="text-lg font-semibold">{result.firstName} {result.lastName}</p>
                                <p className="text-sm text-gray-600">{result.profile.biography || 'Bio.'}</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        );
    };

    const Right = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <WeatherWidget/>
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

export default SearchPage;