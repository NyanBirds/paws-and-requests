import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {createBrowserRouter, RouterProvider} from "react-router";
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import AnimalProfilePage from './pages/AnimalProfilePage.jsx';
import PostsPage from "./pages/PostsPage.jsx";
import {AdoptionPage} from "./pages/AdoptionPage.jsx";
import CreatePost from './pages/CreatePost.jsx';
import {AdoptionFormPage} from "./pages/AdoptionFormsPage.jsx";
import {MyAccountPage} from "./pages/MyAccountPage.jsx";
import {MyAnimalsPage} from "./pages/MyAnimalsPage.jsx";
import {MyPostsPage} from "./pages/MyPostsPage.jsx";
import {MyAdoptionFormsPage} from "./pages/MyAdoptionFormsPage.jsx";

const router = createBrowserRouter([
    {
        path: '/',
        Component: App,
        children: [
            { index: true, Component: HomePage },
            { path: 'registration', Component: RegisterPage },
            { path: 'login', Component: LoginPage },
            { path: 'posts', Component: PostsPage },
            { path: 'posts/:postId', Component: AnimalProfilePage },
            { path: 'posts/:postId/adoption', Component: AdoptionPage },
            { path: 'posts/:postId/adoptionForm', Component: AdoptionFormPage },
            { path: 'post/new', Component: CreatePost },
            { path: 'me', Component: MyAccountPage },
            { path: 'me/animals', Component: MyAnimalsPage },
            { path: 'me/posts', Component: MyPostsPage },
            { path: 'me/adoptionForms', Component: MyAdoptionFormsPage },
        ]
    }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
