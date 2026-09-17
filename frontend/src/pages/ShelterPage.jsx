import { useEffect, useState } from "react";
import { api } from "../api/client";
import { useParams } from "react-router";
import PostDetails from "../components/PostDetails";

export default function ShelterPage() {
    const { orgNr } = useParams()
    const [shelter, setShelter] = useState()
    const [posts, setPosts] = useState([])
    const [error, setError] = useState()

    useEffect(() => {
        api.get(`/shelters/${orgNr}`)
            .then(res => setShelter(res), 
                  err => setError(err))
        api.get(`/posts/shelter/${orgNr}`)
            .then(res => setPosts(res))
            .then(console.log(posts))
    }, [])

    return <>
    { error ? (
        <p>{error.status}</p>
    ) : (
        <div>
            <h1>{shelter?.shelterName}</h1>
            <p>About us: {shelter?.description}</p>
            <p>Address: {shelter?.address}</p>
            <section>
                <h2>Posts</h2>
                {posts.map(post => <PostDetails key={post.id} {...post}/>)}
            </section>
        </div>
    )}
    </>
}