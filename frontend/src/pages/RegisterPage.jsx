import { useState } from "react"
import InputField from "../components/InputField"

export default function RegistrationPage() {
    const [formData, setFormData] = useState({})
    const [submitted, setSubmitted] = useState(false)

    const inputFields = [
        {key: 'firstName', type: 'text', label: 'First name:'},
        {key: 'lastName', type: 'text', label: 'Last name:'},
        {key: 'email', type: 'text', label: 'Email:'},
        {key: 'phoneNumber', type: 'text', label: 'Phone number:'},
        {key: 'password', type: 'password', label: 'Password:'},
        {key: 'profilePicture', type: 'file', label: 'Profile picture:'},
    ]

    function onChange(key, value) {
        setFormData({...formData, [key]: value})
    }

    function handleSubmit(event) {
        event.preventDefault()
        setSubmitted(true)
        // handle registration logic
    }

    return (
    <div>
        {submitted ? (
            <h2>Thank you for registering on our page!</h2>
        ) : (
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
        )}
    </div>
  )
}
