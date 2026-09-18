import {useEffect, useState} from "react";
import PostDetails from "../components/PostDetails.jsx";
import CardGrid from "../components/CardGrid.jsx";
import PostCard from "../components/PostCard.jsx";

export default function PostsPage() {

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
            <CardGrid> {posts.map(post => <PostCard key={post.id} {...post}/>)} </CardGrid>
        </section>
    );
}
