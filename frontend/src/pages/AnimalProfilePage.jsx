import {useEffect, useState} from "react";
import {useNavigate, useParams, useOutletContext } from "react-router";
import Divider from "../components/Divider.jsx";
import {BASE_URL} from "../api/client.js";
import Gallery from "../components/Gallery.jsx";
import {deletePost, getPost} from "../services/postService.js";
import CheckUser from "../components/CheckUser.jsx";
import "../components/Button.css";

export default function AnimalProfilePage() {
    const { isLoggedIn } = useOutletContext();
    const user = CheckUser(isLoggedIn);
    const role = user?.role;

    const navigate = useNavigate();
    const { postId } = useParams();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        getPost(postId)
            .then(post => setPost(post))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, [postId]);

    if (error) {
        return <p>Could not load this post: {error}</p>;
    }

    if (loading) {
        return <p>Loading...</p>;
    }

    const canAccess = role === "ADMIN" || (role === "SHELTERUSER" && user?.orgNr === post.orgNr);

    return (
        <section>
            <h1>{post.animalName}</h1>
            {canAccess && (
                <div style={{
                    display: "flex",
                    gap: "12px",
                    justifyContent: "center",
                    alignItems: "center",
                    flexWrap: "wrap",
                    marginBottom: "32px"
                }}>
                <button
                    className="btn btn-danger"
                    onClick={() => { deletePost(postId).then(_ => navigate(`/`))
                }}>Delete Post</button>

                <button
                    className={`btn btn-primary`}
                    onClick={() => navigate(`/posts/${postId}/edit`)
                }>Edit post</button>
                </div>
            )}
            <div>
                <Gallery image={post.url.map(imageUrl => `${BASE_URL}${imageUrl}`)}/>
            </div>
            <h3>Age: {post.age}</h3>
            <h3>Gender: {post.gender}</h3>
            <h3>Species: {post.species}</h3>
            <Divider/>
            <h2>{post.title}</h2>
            <p style={{
                maxWidth: "640px",
                margin: "0 auto 24px",
                overflowWrap: "anywhere"
            }}>{post.description}</p>
            <Divider/>
            <p>Shelter: {post.shelterName}</p>
            <p style={{padding: '0 24px 48px'}}>Address: {post.address}</p>
            {role === "USER" && (
                <button
                    className={`btn btn-primary`}
                    onClick={() => navigate(`/posts/${postId}/adoption`)}>Adopt</button>
            )}
            {canAccess && (
                <button
                    style={{margin: '10px'}}
                    className={`btn btn-primary`}
                    onClick={() => navigate(`/posts/${postId}/adoptionForm`)}>View Adoption Forms</button>
            )}
        </section>
    );
}
