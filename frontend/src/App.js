import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthPage from "./page/AuthPage";
import HomePage from "./page/HomePage";
import ProfilePage from "./page/ProfilePage";
import SearchPage from "./page/SearchPage";
import FollowPage from "./page/FollowPage";
import PostPage from "./page/PostPage";
import MessagePage from "./page/MessagePage";
import {AuthContextProvider} from "./context/AuthContext";
import {FollowProvider} from "./context/FollowContext";
import PrivateRoute from "./util/PrivateRoute";
import {Toaster} from "./components/ui/toaster";
import {PostProvider} from "./context/PostContext";

function App() {
    return (
        <AuthContextProvider>
            <FollowProvider>
                <PostProvider>
                    <BrowserRouter>
                        <Toaster />
                        <Routes>
                            <Route path="/" element={<AuthPage/>}/>
                            <Route element={<PrivateRoute />}>
                                <Route path="/home" element={<HomePage />} />
                                <Route path="/profile" element={<ProfilePage />} />
                                <Route path="/search" element={<SearchPage />} />
                                <Route path="/follow" element={<FollowPage />} />
                                <Route path="/post" element={<PostPage />} />
                                <Route path="/message" element={<MessagePage />} />
                            </Route>
                        </Routes>
                    </BrowserRouter>
                </PostProvider>
            </FollowProvider>
        </AuthContextProvider>
    );
}

export default App;
