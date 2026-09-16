import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { animalProfile } from "../api/pages.js";
import Divider from "../components/Divider.jsx";

export default function AnimalProfilePage() {
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
                {post.url?.map((imageUrl) => (
                    <img key={imageUrl} src={imageUrl} alt={post.animalName} width={200}/>
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
            <button>Adopt</button>
        </section>
    );
}
