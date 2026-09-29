import {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {newPost} from "../services/postService.js";
import {Box} from "../components/Box.jsx";
import InputField from "../components/InputField.jsx";
import {fetchMe} from "../services/authService.js";
import {editUser} from "../services/userService.js";

export function EditAccountPage() {

    const navigate = useNavigate();
    const [id, setId] = useState(null);
    useEffect(() => {
        fetchMe()
            .then(response => setId(response.id))
            .then(response => console.log(response));
    }, [])
    const [formData, setFormData] = useState({})

    const inputFields = [
        { key: 'password', type: 'password', label: 'Password', required: false },
        { key: 'phoneNumber', type: 'text', label: 'Phone Nr', required: false }
    ]

    function onChange(key, value) {
        setFormData({ ...formData, [key]: value })
    }
    function onSubmit(event) {
        event.preventDefault();

        console.log(formData);

        editUser(id, formData)
            .then(() => {
                navigate('/me')
            });
    }

    return (
        <div>
            <Box width="40%">
                <h3>Edit User Profile</h3>
                <form onSubmit={onSubmit}>
                    {inputFields.map((field) => (
                        <InputField
                            name={field.key}
                            type={field.type}
                            label={field.label}
                            required={field.required}
                            onChange={onChange} />
                    ))}
                    <button type="submit">Submit</button>
                </form>
            </Box>
        </div>
    );
}
