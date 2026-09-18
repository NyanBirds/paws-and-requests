import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {createBrowserRouter, RouterProvider} from "react-router";
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import AnimalProfilePage from './pages/AnimalProfilePage.jsx';
import PostsPage from "./pages/PostPage.jsx";
import ShelterPage from './pages/ShelterPage.jsx';
import ShelterListPage from './pages/ShelterListPage.jsx';

const router = createBrowserRouter([
    {
        path: '/',
        Component: App,
        children: [
            { index: true, Component: HomePage },
            { path: 'registration', Component: RegisterPage },
            { path: 'login', Component: LoginPage },
            { path: 'posts/:postId', Component: AnimalProfilePage },
            { path: 'posts', Component: PostsPage },
            { path: 'shelters', Component: ShelterListPage},
            { path: 'shelters/:orgNr', Component: ShelterPage}
        ]
    }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
