import { useState } from "react"
import InputField from "../components/InputField"
import { register } from "../api/auth"
import { useNavigate } from "react-router"
import {Box} from "../components/Box.jsx";

export default function RegistrationPage() {
    const [formData, setFormData] = useState({})
    const [error, setError] = useState("")
    const navigate = useNavigate();
    const [shelterRegistration, setShelterRegistration] = useState(false)

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

    const toInputField =
        (inputField) => (
                <InputField
                    key = {inputField.key}
                    name = {inputField.key}
                    type = {inputField.type}
                    label = {inputField.label}
                    required = {inputField.required}
                    onChange = {onChange}/>
            )

    return (
    <div>
        <Box>
            <h3>Registration</h3>
            <button
                onClick={() => setShelterRegistration(!shelterRegistration)}
            >{shelterRegistration ? (<span>User registration</span>) : (<span>Shelter registration</span>)}</button>
            <form onSubmit={onSubmit}>
                {inputFields.map(toInputField)}
                {shelterRegistration && shelterFields.map(toInputField)}
                <button type="submit">Submit</button>
                <p>{error.message}</p>
            </form>
        </Box>
    </div>
  )
}
