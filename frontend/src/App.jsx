import './App.css'
import {Link, NavLink, Outlet, useNavigate} from "react-router";
import {useEffect, useState} from "react";
import logo from "./assets/logo_transparent.png"
import CheckUser from "./components/CheckUser.jsx";
import {fetchMe} from "./services/authService.js";

const AUTH_TOKEN = "authToken";
const AUTH_EVENT = "authorization-request";

function App() {

    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const user = CheckUser(isLoggedIn);
    const role = user?.role;

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
        <div className="row">
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
              <NavLink to="/shelters">Shelters</NavLink>
              {" | "}
              {role === 'SHELTERUSER' && (
                  <>
                    <NavLink to="/post/new">Create post</NavLink>
                    {" | "}
                  </>
              )}
              {isLoggedIn ? (
                  <>
                    <span onClick={() => navigate("/me")}
                    >Hello {user?.firstName}</span>
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
