export default function InputField({name, type, label, required, onChange}) {
    return <>
        <label>{label}
            <input
                name={name}
                type={type}
                required={required}
                onChange={(event) => onChange(name, event.target.value)}
            />
        </label>
    </>
}
