import './App.css'
import {NavLink, Outlet} from "react-router";

function App() {

  return (
    <>
      <nav>
        <NavLink to="/">Home</NavLink>
        {" | "}
        <NavLink to="/registration">Register</NavLink>
        {" | "}
        <NavLink to="/login">Login</NavLink>
      </nav>
      <Outlet/>
    </>
  )
}

export default App
