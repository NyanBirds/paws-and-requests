import {useEffect, useState} from "react";
import PostCard from "../components/PostCard.jsx";
import CardGrid from "../components/CardGrid.jsx";
import {getShelterPosts} from "../services/postService.js";

export function MyPostsPage() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        getShelterPosts()
            .then(posts => setPosts(posts))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
        console.log(posts);
    }, []);

    if (error) return <p>Could not load posts: {error}</p>;

    if (loading) return <p>Loading...</p>;

    return (
        <>
            <h1>All Shelter Posts</h1>
            {posts.length ? (
                <CardGrid> {posts.map(post => <PostCard key={post.id} {...post}/>)} </CardGrid>
            ) : (
              <p>No posts yet</p>
            )}
        </>
    );
}
