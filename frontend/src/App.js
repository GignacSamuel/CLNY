import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthPage from "./page/AuthPage";
import HomePage from "./page/HomePage";
import ProfilePage from "./page/ProfilePage";
import SearchPage from "./page/SearchPage";
import FollowPage from "./page/FollowPage";
import {AuthContextProvider} from "./context/AuthContext";
import {FollowProvider} from "./context/FollowContext";
import PrivateRoute from "./util/PrivateRoute";
import {Toaster} from "./components/ui/toaster";

function App() {
    return (
        <AuthContextProvider>
            <FollowProvider>
                <BrowserRouter>
                    <Toaster />
                    <Routes>
                        <Route path="/" element={<AuthPage/>}/>
                        <Route element={<PrivateRoute />}>
                            <Route path="/home" element={<HomePage />} />
                            <Route path="/profile" element={<ProfilePage />} />
                            <Route path="/search" element={<SearchPage />} />
                            <Route path="/follow" element={<FollowPage />} />
                        </Route>
                    </Routes>
                </BrowserRouter>
            </FollowProvider>
        </AuthContextProvider>
    );
}

export default App;
