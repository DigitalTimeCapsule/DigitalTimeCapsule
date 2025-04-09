import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import './App.css';
import Login from "./pages/Login/Login";
import HomePage from "./pages/HomePage/HomePage";
import CreateCapsulePage from "./pages/CreateCapsulePage/CreateCapsulePage";
import CapsuleManagerPage from "./pages/CapsuleManagerPage/CapsuleManagerPage";
import PrivateRoute from "./components/PrivateRoute";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/capsule-manager" element={
                    <PrivateRoute>
                        <CapsuleManagerPage/>
                    </PrivateRoute>
                }/>

                <Route path="/capsule/create" element={
                    <PrivateRoute>
                        <CreateCapsulePage/>
                    </PrivateRoute>
                }/>
            </Routes>
        </Router>
    );
}

export default App;
