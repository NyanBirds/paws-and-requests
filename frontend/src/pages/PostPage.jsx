import {useEffect, useState} from "react";
import PostDetails from "../components/PostDetails.jsx";

export default function AdoptionPage() {

    const [posts, setPosts] = useState([]);

    useEffect(() => {
        async function fetchPosts() {
            const response = await fetch("http://localhost:8080/posts");
            const data = await response.json();

            setPosts(data);
        }

        fetchPosts();
    }, []);

    return(
        <section>
            <h1>Look at all these cuties!</h1>
            <ul>
                {posts.map(post => <PostDetails key={post.postId} {...post}/>)}
            </ul>
        </section>
    );
}
