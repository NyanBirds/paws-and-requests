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
import ShelterPage from './pages/ShelterPage.jsx';
import ShelterListPage from './pages/ShelterListPage.jsx';
import {AdoptionPage} from "./pages/AdoptionPage.jsx";
import CreatePost from './pages/CreatePost.jsx';
import {AdoptionFormPage} from "./pages/AdoptionFormsPage.jsx";
import {MyAccountPage} from "./pages/MyAccountPage.jsx";
import {MyAnimalsPage} from "./pages/MyAnimalsPage.jsx";
import {MyPostsPage} from "./pages/MyPostsPage.jsx";
import {MyAdoptionFormsPage} from "./pages/MyAdoptionFormsPage.jsx";
import {EditAccountPage} from "./pages/EditAccountPage.jsx";
import {EditPost} from "./pages/EditPost.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import NoAccessPage from "./pages/NoAccessPage.jsx";

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
            { path: 'shelters', Component: ShelterListPage},
            { path: 'shelters/:orgNr', Component: ShelterPage},
            { path: 'no-access', Component: NoAccessPage},

            {
                Component: ProtectedRoute,
                children: [
                    { path: 'me', Component: MyAccountPage },
                    { path: 'me/edit', Component: EditAccountPage },

                ]
            },

            {
                element: <ProtectedRoute roles={["SHELTERUSER"]} />,
                children: [
                    { path: 'post/new', Component: CreatePost },
                ]
            },

            {
                element: <ProtectedRoute roles={["SHELTERUSER", "ADMIN"]} />,
                children: [
                    { path: 'me/posts', Component: MyPostsPage },
                    { path: 'posts/:postId/adoptionForm', Component: AdoptionFormPage },
                    { path: 'posts/:postId/edit', Component: EditPost },
                    { path: 'me/animals', Component: MyAnimalsPage },
                ]
            },

            {
                element: <ProtectedRoute roles={["USER"]} />,
                children: [
                    { path: 'posts/:postId/adoption', Component: AdoptionPage },
                    { path: 'me/adoptionForms', Component: MyAdoptionFormsPage },
                ]
            },
        ]
    }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
