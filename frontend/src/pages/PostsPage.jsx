import {useEffect, useState} from "react";
import PostDetails from "../components/PostDetails.jsx";
import {getAllPosts} from "../services/postService.js";

export default function PostsPage() {

    const [posts, setPosts] = useState([]);

    useEffect(() => {
        getAllPosts()
            .then(posts => setPosts(posts));
    }, []);

    return(
        <section>
            <h1>Look at all these cuties!</h1>
            <ul>
                {posts.map(post => <PostDetails key={post.id} {...post}/>)}
            </ul>
        </section>
    );
}
