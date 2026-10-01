export default function InputField({
    className,
    name,
    type = "text",
    label,
    required = true,
    onChange,
    defaultValue,
    error,
}) {
    return <div className={className}>
        <label>{label}</label>
        <input
            name={name}
            type={type}
            required={required}
            // aria-invalid and aria-describedby tie the server-side message to
            // the input, so a screen reader announces why the field was refused.
            aria-invalid={error ? "true" : undefined}
            aria-describedby={error ? `${name}-error` : undefined}
            onChange={(event) => onChange(
                name,
                type == "file" ?
                Array.from(event.target.files) : event.target.value
            )}
            defaultValue={defaultValue}
            multiple={type=="file"}
        />
        {error && <p className="field-error" id={`${name}-error`}>{error}</p>}
    </div>
}