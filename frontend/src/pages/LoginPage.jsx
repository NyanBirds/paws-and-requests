import { useState } from "react"
import InputField from "../components/InputField"

export default function LoginPage() {
    const [formData, setFormData] = useState({})
    const [submitted, setSubmitted] = useState(false)

    const inputFields = [
        {key: 'email', type: 'text', label: 'Email:'},
        {key: 'password', type: 'password', label: 'Password:'},
    ]

    function onChange(key, value) {
        setFormData({...formData, [key]: value})
    }

    function handleSubmit(event) {
        event.preventDefault()
        setSubmitted(true)
        // handle login logic
    }

    return (
    <div>
        {submitted ? (
            <h2>Thank you for logging in!</h2>
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
