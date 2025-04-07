import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import './App.css';
import Login from "./pages/Login/Login";
import HomePage from "./pages/HomePage/HomePage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/login" element={<Login/>}/>
            </Routes>
        </Router>
    );
}

export default App;
