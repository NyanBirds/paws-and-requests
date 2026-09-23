export default function TextField({name, label, required = true, onChange}) {
    return <div>
        <label>{label}</label>
        <textarea
            name={name}
            required={required}
            onChange={(event) => onChange(name, event.target.value)}
        />
    </div>
}
