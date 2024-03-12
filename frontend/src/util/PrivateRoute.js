import {useContext} from "react";
import {AuthContext} from "../context/AuthContext";
import {Navigate, Outlet} from "react-router-dom";

const PrivateRoute = () => {
    const { user, token } = useContext(AuthContext);

    return user && token ? <Outlet /> : <Navigate to="/" replace />;
}

export default PrivateRoute;