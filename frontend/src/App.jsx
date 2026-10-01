import './App.css'
import {Outlet, useNavigate} from "react-router";
import {useEffect, useState} from "react";
import CheckUser from "./components/CheckUser.jsx";
import {fetchMe} from "./services/authService.js";
import Navbar from "./components/Navbar.jsx";
import Footer from "./components/Footer.jsx";

const AUTH_TOKEN = "authToken";
const AUTH_EVENT = "authorization-request";

function App() {

    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(
        () => Boolean(localStorage.getItem(AUTH_TOKEN))
    );
    const user = CheckUser(isLoggedIn);

    useEffect(() => {
        if (Boolean(localStorage.getItem(AUTH_TOKEN))) {
            fetchMe()
                .then(() => {
                    setIsLoggedIn(true);
                })
                .catch((error) => {
                    console.log(error);
                    localStorage.removeItem(AUTH_TOKEN);
                    setIsLoggedIn(false);
                });
        }
        const sync = () => setIsLoggedIn(Boolean(localStorage.getItem(AUTH_TOKEN)));
        window.addEventListener(AUTH_EVENT, sync);
        return () => window.removeEventListener(AUTH_EVENT, sync);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem(AUTH_TOKEN);
        window.dispatchEvent(new Event(AUTH_EVENT));
        navigate("/");
    }

  return (
    <>
      <Navbar isLoggedIn={isLoggedIn} user={user} onLogout={handleLogout}/>
      <main style={{ flex: 1 }}>
        <Outlet context={{ isLoggedIn, user }}/>
      </main>
      <Footer/>
    </>
  )
}

export default App
