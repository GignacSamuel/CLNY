import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthPage from "./page/AuthPage";
import HomePage from "./page/HomePage";
import {AuthContextProvider} from "./context/AuthContext";

function App() {
  return (
      <AuthContextProvider>
          <BrowserRouter>
              <Routes>
                  <Route path="/" element={<AuthPage/>}/>
                  <Route path="/home" element={<HomePage/>}/>
              </Routes>
          </BrowserRouter>
      </AuthContextProvider>
  );
}

export default App;
