import {useEffect, useState} from "react";
import PostCard from "../components/PostCard.jsx";
import CardGrid from "../components/CardGrid.jsx";
import {getShelterPosts} from "../services/postService.js";

export function MyPostsPage() {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        getShelterPosts()
            .then(posts => setPosts(posts));
        console.log(posts);
    }, []);

    return (
        <>
            <h1>All Shelter Posts</h1>
            <CardGrid> {posts.map(post => <PostCard key={post.id} {...post}/>)} </CardGrid>
        </>
    );
}
