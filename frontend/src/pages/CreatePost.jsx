import { useState, useEffect } from "react"
import InputField from "../components/InputField";
import TextField from "../components/TextField.jsx"
import { useNavigate } from "react-router"
import DropDown from "../components/DropDown.jsx";
import '../components/Form.css';
import {getAnimals} from "../services/animalService.js";
import {newPost} from "../services/postService.js";

export default function CreatePost() {
    const [formData, setFormData] = useState({})
    const [animals, setAnimals] = useState([])
    const [status, setStatus] = useState("idle");
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const inputFields = [
        { key: 'title', label: 'Title:', required: true },
        { key: 'description', label: 'Description:', required: true },
        { key: 'url', type: 'file', label: 'Pictures', required: false },
        { key: 'animalId', label: 'Animal', required: true }
    ]

    useEffect(() => {
        getAnimals()
            .then(response => setAnimals(response));
    }, [])

    function onChange(key, value) {
        setFormData({ ...formData, [key]: value })
    }
    function onSubmit(event) {
        event.preventDefault();
        setStatus('submitting');

        const { url: pictureFiles = [], ...postFields} = formData;
        console.log(formData);
        newPost(postFields, pictureFiles)
            .then((post) => {
                setStatus("success");
                navigate(`/posts/${post.id}`)
            }, (error) => {
                setStatus("error");
                setMessage(error.message);
                console.log(error.message);
            });
    }
    return (
        <div className="form-container">
            <form className="custom-form" onSubmit={onSubmit}>
                <h2>Create a post</h2>
                <p className="form-intro">
                    Tell people about an animal that is looking for a new home.
                </p>

                <InputField
                    className="form-group"
                    name={inputFields[0].key}
                    label={inputFields[0].label}
                    onChange={onChange} />

                <DropDown
                    className="form-group"
                    name={inputFields[3].key}
                    items={animals}
                    label={inputFields[3].label}
                    onChange={onChange}
                    />

                <TextField
                    className="form-group"
                    name={inputFields[1].key}
                    label={inputFields[1].label}
                    onChange={onChange}
                    placeholder="Describe the animal"
                />

                <InputField
                    className="form-group"
                    name={inputFields[2].key}
                    type={inputFields[2].type}
                    label={inputFields[2].label}
                    onChange={onChange}/>

                <button type="submit" className="submit-btn" disabled={status === "submitting"}>
                    {status === "submitting" ? "Creating..." : "Create post"}
                </button>
                {status === "error" && (
                    <p className="form-error" role="alert">{message}</p>
                )}
            </form>
        </div>
    );
}


