import './App.css'
import {Link, NavLink, Outlet, useNavigate} from "react-router";
import {useEffect, useState} from "react";
import logo from "./assets/logo_transparent.png"
import CheckRole from "./components/CheckRole.jsx";

const AUTH_TOKEN = "authToken";
const AUTH_EVENT = "authorization-request";

function App() {

    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(Boolean(localStorage.getItem(AUTH_TOKEN)));
    const user = CheckRole(isLoggedIn);
    const role = user?.role;

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
        <div style={{ display: 'flex', gap: '30rem' }}>
        <Link to='/'>
            <img
                src={logo}
                width="100"
                style={{ padding: '20px' }}
            />
        </Link>
          <nav>
              <NavLink to="/posts">Posts</NavLink>
              {" | "}
              {role === 'SHELTERUSER' && (
                  <>
                    <NavLink to="/post/new">Create post</NavLink>
                    {" | "}
                  </>
              )}
              {isLoggedIn ? (
                  <>
                    <span>Hello {user?.firstName}</span>
                    {" | "}
                    <button type="button" onClick={handleLogout}>Logout</button>
                  </>
              ) : (
                  <>
                  <NavLink to="/registration">Register</NavLink>
                    {" | "}
                  <NavLink to="/login">Login</NavLink>
                  </>
              )}
          </nav>
        </div>
      <Outlet context={{ isLoggedIn }}/>
    </>
  )
}

export default App
