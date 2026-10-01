import {useEffect, useState} from "react";
import PostCard from "../components/PostCard.jsx";
import CardGrid from "../components/CardGrid.jsx";
import {getPosts, getShelterPosts} from "../services/postService.js";
import {useOutletContext} from "react-router";

export function MyPostsPage() {
    const { user } = useOutletContext();
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const request = user?.role === "ADMIN"
            ? getPosts("")
            : getShelterPosts();
        request
            .then(posts => setPosts(posts))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
        console.log(posts);
    }, [user]);

    if (error) return <p>Could not load posts: {error}</p>;

    if (loading) return <p>Loading...</p>;

    return (
        <>
            <div style={{padding: '0 24px 48px'}}>
                <h1>{user?.role === "ADMIN" ? "All posts" : "My posts"}</h1>

                {posts.length ? (
                    <CardGrid> {posts.map(post => <PostCard key={post.id} {...post}/>)} </CardGrid>
                ) : (
                <p>No posts yet</p>
                )}
            </div>
        </>
    );
}
