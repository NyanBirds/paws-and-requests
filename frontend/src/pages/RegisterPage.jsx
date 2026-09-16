import { useState } from "react"
import InputField from "../components/InputField"
import { register } from "../api/auth"
import { useNavigate } from "react-router"

export default function RegistrationPage() {
    const [formData, setFormData] = useState({})
    const [error, setError] = useState("")
    const navigate = useNavigate();

    const inputFields = [
        {key: 'firstname', type: 'text', label: 'First name:', required: true},
        {key: 'lastname', type: 'text', label: 'Last name:', required: true},
        {key: 'email', type: 'text', label: 'Email:', required: true},
        {key: 'phonenumber', type: 'text', label: 'Phone number:', required: true},
        {key: 'password', type: 'password', label: 'Password:', required: true},
    ]

    function onChange(key, value) {
        setFormData({...formData, [key]: value})
    }

    function onSubmit(event) {
        event.preventDefault()
        register(formData)
            .then(res => {
                localStorage.setItem("authToken", res.token)
                window.dispatchEvent(new Event("authorization-request"))
                navigate('/')
            }, (err) => {
                setError(err)
            })
    }

    return (
    <div>
        <p>{error.message}</p>
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
            <button type="submit">Submit</button>
        </form>
    </div>
  )
}
