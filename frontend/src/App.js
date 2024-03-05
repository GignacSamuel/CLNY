import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthPage from "./page/AuthPage";

function App() {
  return (
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<AuthPage/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
