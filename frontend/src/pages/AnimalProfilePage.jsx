import {useEffect, useState} from "react";
import {useNavigate, useParams, useOutletContext } from "react-router";
import Divider from "../components/Divider.jsx";
import {BASE_URL} from "../api/client.js";
import Gallery from "../components/Gallery.jsx";
import {getPost} from "../services/postService.js";
import CheckUser from "../components/CheckUser.jsx";

export default function AnimalProfilePage() {
    const { isLoggedIn } = useOutletContext();
    const role = CheckUser(isLoggedIn)?.role;

    const navigate = useNavigate();
    const { postId } = useParams();
    const [post, setPost] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        getPost(postId)
            .then(post => setPost(post))
            .catch(error => setError(error.message));
    }, [postId]);

    if (error) {
        return <p>Could not load this post: {error}</p>;
    }

    if (!post) {
        return <p>Loading...</p>;
    }

    return (
        <section>
            <h1>{post.animalName}</h1>
            <div>
                <Gallery image={post.url.map(imageUrl => `${BASE_URL}${imageUrl}`)}/>
            </div>
            <h2>{post.title}</h2>
            <p>{post.description}</p>
            <Divider/>
            <h3>Age: {post.age}</h3>
            <h3>Gender: {post.gender}</h3>
            <h3>Species: {post.species}</h3>
            <Divider/>
            <p>Shelter: {post.shelterName}</p>
            <p>Address: {post.address}</p>
            {role === "USER" && (
                <button onClick={() => navigate(`/posts/${postId}/adoption`)}>Adopt</button>
            )}
            {(role === "SHELTERUSER" || role === "ADMIN") && (
                <button onClick={() => navigate(`/posts/${postId}/adoptionForm`)}>View Adoption Forms</button>
            )}
        </section>
    );
}
