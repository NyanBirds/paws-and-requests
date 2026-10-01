import {useState} from "react"
import InputField from "../components/InputField"
import {login} from "../services/authService.js"
import {useLocation, useNavigate} from "react-router"
import '../components/Button.css';
import '../components/Form.css';

export default function LoginPage() {
    const [formData, setFormData] = useState({})
    const [status, setStatus] = useState("idle");
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const originalRoute = useLocation();
    const from = originalRoute.state?.from?.pathname || "/";

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
                navigate(from, { replace: true })
            }, (error) => {
                setStatus("error");
                setMessage(error.mesage);
            })
    }

    return (
        <div
            className="form-container"
        >
            <form
                className="custom-form"
                onSubmit={onSubmit}
            >
                <h2>Login</h2>
                {inputFields.map((inputField) => (
                    <InputField
                        className="form-group"
                        key = {inputField.key}
                        name = {inputField.key}
                        type = {inputField.type}
                        label = {inputField.label}
                        required = {inputField.required}
                        onChange = {onChange}/>
                ))}
                <div className="row">
                    <button
                        className="submit-btn"
                        type="submit" disabled={status === "submitting"}>
                        {status === "submitting" ? "Logging in..." : "Login"}
                    </button>
                    <p onClick={ () => navigate('/registration') }>No account? Register here</p>
                </div>
            </form>
            {status === "error" && <p>{message}</p>}
        </div>
  )
}
