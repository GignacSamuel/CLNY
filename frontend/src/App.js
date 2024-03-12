import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthPage from "./page/AuthPage";
import HomePage from "./page/HomePage";
import {AuthContextProvider} from "./context/AuthContext";
import PrivateRoute from "./util/PrivateRoute";

function App() {
  return (
      <AuthContextProvider>
          <BrowserRouter>
              <Routes>
                  <Route path="/" element={<AuthPage/>}/>
                  <Route element={<PrivateRoute />}>
                      <Route path="/home" element={<HomePage />} />
                  </Route>
              </Routes>
          </BrowserRouter>
      </AuthContextProvider>
  );
}

export default App;
