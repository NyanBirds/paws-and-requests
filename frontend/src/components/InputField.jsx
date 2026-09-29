export default function InputField({name, type = "text", label, required = true, onChange, defaultValue}) {
    return <div>
        <label>{label}</label>
        <input
            name={name}
            type={type}
            required={required}
            onChange={(event) => onChange(
                name, 
                type == "file" ? 
                Array.from(event.target.files) : event.target.value
            )}
            defaultValue={defaultValue}
            multiple={type==="file"}
        />
    </div>
}
