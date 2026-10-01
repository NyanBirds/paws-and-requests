import { useState } from "react"
import InputField from "../components/InputField"
import { register } from "../services/authService.js"
import { useLocation, useNavigate } from "react-router"
import '../components/Form.css';
import ToggleButton from "../components/ToggleButton.jsx";

export default function RegistrationPage() {
    const [formData, setFormData] = useState({})
    // Per-field messages from a 400, keyed by input name so each one can be
    // rendered under its own input.
    const [fieldErrors, setFieldErrors] = useState({})
    // Anything that is not field-level, such as the 409 for an email that is
    // already registered.
    const [error, setError] = useState("")
    const navigate = useNavigate();
    const location = useLocation();
    const [shelterRegistration, setShelterRegistration] = useState(
        Boolean(location.state?.shelter)
    )

    const inputFields = [
        {key: 'firstname', type: 'text', label: 'First name:', required: true},
        {key: 'lastname', type: 'text', label: 'Last name:', required: true},
        {key: 'email', type: 'text', label: 'Email:', required: true},
        {key: 'phonenumber', type: 'text', label: 'Phone number:', required: true},
        {key: 'password', type: 'password', label: 'Password:', required: true},
    ]

    const shelterFields = [
        {key: 'shelterOrg', type: 'text', label: 'Shelter Org', required: true}
    ]

    function onChange(key, value) {
        // Clear this field's message as soon as the user edits it, so the form
        // stops showing complaints the user has already addressed.
        setFieldErrors((previous) => {
            if (!previous[key]) {
                return previous
            }
            const next = {...previous}
            delete next[key]
            return next
        })
        setFormData({...formData, [key]: value})
    }

    function onSubmit(event) {
        event.preventDefault()
        setError("")
        setFieldErrors({})
        register(formData)
            .then(res => {
                localStorage.setItem("authToken", res.token)
                window.dispatchEvent(new Event("authorization-request"))
                navigate('/')
            }, (err) => {
                const fields = err.body?.fields
                if (fields && Object.keys(fields).length > 0) {
                    setFieldErrors(fields)
                } else {
                    setError(err.message)
                }
            })
    }

    const toInputField =
        (inputField) => (
                <InputField
                    className="form-group"
                    key = {inputField.key}
                    name = {inputField.key}
                    type = {inputField.type}
                    label = {inputField.label}
                    required = {inputField.required}
                    error = {fieldErrors[inputField.key]}
                    onChange = {onChange}/>
            )

    return (
    <div className="form-container">
            <form className="custom-form" onSubmit={onSubmit}>
                <h2>Sign up</h2>
                <ToggleButton
                    label="Shelter registration"
                    isOn={shelterRegistration}
                    onToggle={() => setShelterRegistration(!shelterRegistration)}/>
                {inputFields.map(toInputField)}
                {shelterRegistration && shelterFields.map(toInputField)}
                <button type="submit" className="submit-btn">Register</button>
                {error && <p>{error}</p>}
            </form>

    </div>
  )
}