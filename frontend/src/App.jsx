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
      <h1>Paws and Requests</h1>
      <Outlet/>
    </>
  )
}

export default App
