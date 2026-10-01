import {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import InputField from "../components/InputField.jsx";
import "../components/Form.css";
import {fetchMe} from "../services/authService.js";
import {editUser} from "../services/userService.js";

export function EditAccountPage() {

    const navigate = useNavigate();
    const [id, setId] = useState(null);

    const [status, setStatus] = useState("idle");
    const [message, setMessage] = useState('');

    useEffect(() => {
        fetchMe()
            .then(response => setId(response.id))
            .then(response => console.log(response));
    }, [])
    const [formData, setFormData] = useState({})

    const inputFields = [
        { key: 'password', type: 'password', label: 'New password', required: false },
        { key: 'phoneNumber', type: 'text', label: 'Phone number', required: false }
    ]

    function onChange(key, value) {
        setFormData({ ...formData, [key]: value })
    }
    function onSubmit(event) {
        event.preventDefault();
        setStatus('submitting');

        editUser(id, formData)
            .then(() => {
                setStatus("success");
                navigate('/me')
            }, (error) => {
                setStatus("error");
                setMessage(error.message);
            });
    }

    return (
        <div className="form-container">
            <form className="custom-form" onSubmit={onSubmit}>
                <h2>Edit profile</h2>
                <p className="form-intro">
                    Only fill in what you want to change.
                </p>
                {inputFields.map((field) => (
                    <InputField
                        key={field.key}
                        className="form-group"
                        name={field.key}
                        type={field.type}
                        label={field.label}
                        required={field.required}
                        onChange={onChange} />
                ))}
                <button type="submit" className="submit-btn" disabled={status === "submitting"}>
                    {status === "submitting" ? "Saving..." : "Save changes"}
                </button>
                {status === "error" && (
                    <p className="form-error" role="alert">{message}</p>
                )}
            </form>
        </div>
    );
}
