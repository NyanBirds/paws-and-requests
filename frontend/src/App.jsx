import './App.css'
import {Link, NavLink, Outlet, useNavigate} from "react-router";
import {useEffect, useState} from "react";
import logo from "./assets/logo.png"

const AUTH_TOKEN = "authToken";
const AUTH_EVENT = "authorization-request";

function App() {

    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(Boolean(localStorage.getItem(AUTH_TOKEN)));

    useEffect(() => {
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
        <div style={{ display: 'flex', gap: '40rem' }}>
        <Link to='/'>
            <img
                src={logo}
                width="100"
                style={{ padding: '20px' }}
            />
        </Link>
          <nav>
              <NavLink to="/">Home</NavLink>
              {" | "}
              <NavLink to="/posts">Posts</NavLink>
              {" | "}
              {isLoggedIn ? (
                  <button type="button" onClick={handleLogout}>Logout</button>
              ) : (
                  <>
                  <NavLink to="/registration">Register</NavLink>
                    {" | "}
                  <NavLink to="/login">Login</NavLink>
                  </>
              )}
          </nav>
        </div>
      <Outlet/>
    </>
  )
}

export default App
