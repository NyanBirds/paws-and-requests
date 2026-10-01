import { useEffect, useState } from "react";
import { api } from "../api/client";
import { useParams } from "react-router";
import PostCard from "../components/PostCard";

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
    }, [orgNr])

    if (!shelter) return <p>Loading...</p>;

    return <>
        { error ? (
            <p>{error.status}</p>
        ) : (
            <div style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: "20px"
            }}>
                <h1 style={{ margin: "50px 0 24px", color: "var(--text-h)" }}>{shelter?.shelterName}</h1>
                <p style={{ margin: "0 0 8px", color: "var(--text)", maxWidth: "720px" }}>About us: {shelter?.description}</p>
                <p style={{ margin: 0, color: "var(--text)" }}>Address: {shelter?.address}</p>
                <section style={{ width: "100%" }}>
                    <h2 style={{ margin: "0 0 16px", color: "var(--text-h)" }}>Posts</h2>
                    <div style={{
                        padding: '0 0 24px 0',
                        display: "flex",
                        flexWrap: "wrap",
                        justifyContent: "center",
                        gap: "16px" }}>
                        {posts.map(post => <PostCard key={post.id} {...post}/>)}
                    </div>
                </section>
            </div>
        )}
    </>
}
