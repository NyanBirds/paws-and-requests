import {useState} from "react"
import InputField from "../components/InputField"
import {login} from "../services/authService.js"
import {useNavigate} from "react-router"
import {Box} from "../components/Box.jsx";

export default function LoginPage() {
    const [formData, setFormData] = useState({})
    const [status, setStatus] = useState("idle");
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const inputFields = [
        {key: 'email', type: 'text', label: 'Email:', required: true},
        {key: 'password', type: 'password', label: 'Password:', required: true},
    ]

    function onChange(key, value) {
        setFormData({...formData, [key]: value})
    }

    function onSubmit(event) {
        event.preventDefault();
        setStatus('submitting');

        login(formData)
            .then(res => {
                setStatus("success");
                localStorage.setItem("authToken", res.token);
                window.dispatchEvent(new Event("authorization-request"));
                navigate('/')
            }, (error) => {
                setStatus("error");
                setMessage(error.mesage);
            })
    }

    return (
    <div>
        <Box width="30%">
            <h3>Login</h3>
            <form onSubmit={onSubmit}>
                {inputFields.map((inputField) => (
                    <InputField
                        key = {inputField.key}
                        name = {inputField.key}
                        type = {inputField.type}
                        label = {inputField.label}
                        required = {inputField.required}
                        onChange = {onChange}/>
                ))}
                <button type="submit" disabled={status === "submitting"}>
                    {status === "submitting" ? "Logging in..." : "Login"}
                </button>
            </form>
            {status === "error" && <p>{message}</p>}
        </Box>
    </div>
  )
}
