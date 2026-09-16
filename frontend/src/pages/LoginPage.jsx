import { useState } from "react"
import InputField from "../components/InputField"
import { login } from "../api/auth"
import { useNavigate } from "react-router"

export default function LoginPage() {
    const [formData, setFormData] = useState({})
    const [error, setError] = useState("")
    const navigate = useNavigate();

    const inputFields = [
        {key: 'email', type: 'text', label: 'Email:'},
        {key: 'password', type: 'password', label: 'Password:'},
    ]

    function onChange(key, value) {
        setFormData({...formData, [key]: value})
    }

    function handleSubmit(event) {
        setError("")
        event.preventDefault()
        login(formData)
            .then(res => {
                localStorage.setItem("authToken", res.token)
                navigate('/')
            }, (err) => {
                setError(err)
            })
    }

    return (
    <div>
        <form onSubmit={handleSubmit}>
            <p>{error.message}</p>
            {inputFields.map((inputField) => (
                <InputField
                    key = {inputField.key}
                    name = {inputField.key}
                    type = {inputField.type}
                    label = {inputField.label}
                    onChange = {onChange}/>
            ))}
            <button type="submit">Submit</button>
        </form>
    </div>
  )
}
