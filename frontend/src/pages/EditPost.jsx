import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import {editPost, deletePicture, getPost} from "../services/postService.js";
import InputField from "../components/InputField.jsx";
import "../components/Form.css";
import "../components/Button.css";
import TextField from "../components/TextField.jsx";
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
            (error) => setError(error.message));
    }

    function onChange(key, value) {
        setFormData({ ...formData, [key]: value});
    }
    if (!post) {
        return <p>Loading...</p>;
    }

    function onDeletePicture(pictureId) {
        deletePicture(postId, pictureId)
            .then(() => setPost({
                ...post,
                pictureIds: post.pictureIds.filter(id => id !== pictureId),
            }),
                (error) => setError(error.message))
    }
    return (
        <div className="form-container">
            <form className="custom-form" onSubmit={onSubmit}>
                <h2>Edit post</h2>
                <p className="form-intro">{post.animalName}</p>

                <InputField
                    className="form-group"
                    name="title"
                    label="Title"
                    required={false}
                    defaultValue={post.title}
                    onChange={onChange} />

                <TextField
                    className="form-group"
                    name="description"
                    label="Description"
                    required={false}
                    defaultValue={post.description}
                    onChange={onChange}
                />

                {post.pictureIds.length > 0 && (
                    <div className="form-group">
                        <label>Current pictures</label>
                        <div className="picture-grid">
                            {post.pictureIds.map(pictureId => (
                                <div key={pictureId} className="picture-item">
                                    <img src={`${BASE_URL}/pictures/${pictureId}`} alt="" />
                                    <button
                                        type="button"
                                        className="btn btn-danger"
                                        onClick={() => onDeletePicture(pictureId)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                <InputField
                    className="form-group"
                    name="pictures"
                    type="file"
                    label="Add pictures"
                    required={false}
                    onChange={onChange}/>

                <button type="submit" className="submit-btn">Save changes</button>
                {error && <p className="form-error" role="alert">{error}</p>}
            </form>
        </div>
    );
}
