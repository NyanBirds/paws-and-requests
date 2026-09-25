import { useState, useEffect } from "react"
import InputField from "../components/InputField";
import TextField from "../components/TextField.jsx"
import { useNavigate } from "react-router"
import { Box } from "../components/Box.jsx";
import DropDown from "../components/DropDown.jsx";
import {getAnimals} from "../services/animalService.js";
import {newPost} from "../services/postService.js";

export default function CreatePost() {
    const [formData, setFormData] = useState({})
    const [animals, setAnimals] = useState([])
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
        const { url: pictureFiles = [], ...postFields} = formData;
        console.log(formData);
        newPost(postFields, pictureFiles)
            .then(() => {
                navigate('/')
            }, (error) => {
                console.log(error.message);
            });
    }
    return (
        <div>
            <Box>
                <h3>Create a post </h3>
                <form onSubmit={onSubmit}>
                    <InputField
                        name={inputFields[0].key}
                        label={inputFields[0].label}
                        onChange={onChange} />

                    <DropDown
                        name={inputFields[3].key}
                        items={animals}
                        label={inputFields[3].label}
                        onChange={onChange}
                        />

                    <TextField
                        name={inputFields[1].key}
                        label={inputFields[1].label}
                        onChange={onChange} />

                    <InputField
                        name={inputFields[2].key}
                        type={inputFields[2].type}
                        animals={animals}
                        label={inputFields[2].label}
                        onChange={onChange}/>

                    <button type="submit">Submit</button>
                </form>
            </Box>
        </div>
    );
}


