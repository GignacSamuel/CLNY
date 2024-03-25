import Header from "../components/Header";
import {useLocation} from "react-router-dom";

function SearchPage() {
    const location = useLocation();
    const searchResults = location.state?.searchResults;

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
            <div className="bg-slate-100 m-6 p-6">Left</div>
        );
    }

    const Search = () => {
        if (!searchResults || searchResults.length === 0) {
            return (
                <div className="bg-slate-100 m-6 p-6 text-gray-600">
                    No results.
                </div>
            );
        }

        return (
            <div className="bg-slate-100 m-6 p-6">
                {searchResults.map(result => (
                    <div key={result.id} className="bg-white p-4 mb-4">
                        <div className="flex items-center space-x-4">
                            <div className="w-16 h-16 relative">
                                <img
                                    className="rounded-full border border-gray-300"
                                    src={result.profile.profilePicture || "/profile_picture_placeholder.jpg"}
                                    alt={`${result.firstName}'s profile`}
                                />
                            </div>
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

export default SearchPage;