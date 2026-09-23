import { useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router";
import { animalProfile } from "../api/pages.js";
import Divider from "../components/Divider.jsx";
import { BASE_URL } from "../api/client.js";
import Gallery from "../components/Gallery.jsx";

export default function AnimalProfilePage() {
    const navigate = useNavigate();
    const { postId } = useParams();
    const [post, setPost] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchPost() {
            try {
                const data = await animalProfile(postId);
                setPost(data);
            } catch (err) {
                setError(err.message);
            }
        }
        fetchPost();
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

                {post.url?.map((imageUrl) => (
                    <img key={imageUrl} src={`${BASE_URL}${imageUrl}`} alt={post.animalName} width={200}/>
                ))}
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
            <button onClick={() => navigate(`/posts/${postId}/adoption`)}>Adopt</button>
        </section>
    );
}
