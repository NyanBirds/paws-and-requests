import { useState } from "react"
import InputField from "../components/InputField"
import { register } from "../api/auth"
import { useNavigate } from "react-router"

export default function RegistrationPage() {
    const [formData, setFormData] = useState({})
    const [error, setError] = useState("")
    const navigate = useNavigate();

    const inputFields = [
        {key: 'firstname', type: 'text', label: 'First name:'},
        {key: 'lastname', type: 'text', label: 'Last name:'},
        {key: 'email', type: 'text', label: 'Email:'},
        {key: 'phonenumber', type: 'text', label: 'Phone number:'},
        {key: 'password', type: 'password', label: 'Password:'},
    ]

    function onChange(key, value) {
        setFormData({...formData, [key]: value})
    }

    function handleSubmit(event) {
        event.preventDefault()
        register(formData)
            .then(res => {
                localStorage.setItem("authToken", res.token)
                navigate('/')
            }, (err) => {
                setError(err)
            })
    }

    return (
    <div>
        <p>{error.message}</p>
        <form onSubmit={handleSubmit}>
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
