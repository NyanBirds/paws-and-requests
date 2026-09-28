import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import {editPost, getPost} from "../services/postService.js";
import {Box} from "../components/Box.jsx";
import InputField from "../components/InputField.jsx";
import TextField from "../components/TextField.jsx";
import Gallery from "../components/Gallery.jsx";
import {BASE_URL} from "../api/client.js";

export function EditPost() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({});
    const { postId } = useParams();
    const [post, setPost] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        getPost(postId)
            .then(post => setPost(post))
            .catch(error => setError(error.message));
    }, [postId]);

    function onSubmit(event) {
        event.preventDefault();
        const { pictures = [], ...postFields } = formData;
        editPost(postId, postFields, pictures)
            .then(() => navigate(`/posts/${postId}`),
            (error) => console.log(error.message));
    }

    function onChange(key, value) {
        setFormData({ ...formData, [key]: value});
    }
    if (!post) {
        return <p>Loading...</p>;
    }

    return (
        <Box>
            <h1>Edit post</h1>
            <h2>{post.animalName}</h2>
            <form onSubmit={onSubmit}>
                    <InputField
                        name="title"
                        label="Title"
                        required={false}
                        defaultValue={post.title}
                        onChange={onChange} />

                    <TextField
                        name="description"
                        label="Description"
                        required={false}
                        defaultValue={post.description}
                        onChange={onChange}
                    />
                    <div>
                        <Gallery image={post.url.map(imageUrl => `${BASE_URL}${imageUrl}`)}/>
                    </div>

                    <InputField
                        name="pictures"
                        type="file"
                        label="Add pictures"
                        required={false}
                        onChange={onChange}/>

                    <button type="Save">Save</button>
            </form>
        </Box>
    );
}
