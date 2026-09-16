export default function InputField({name, type, label, onChange}) {
    return <>
        <label>{label}
            <input
                name={name}
                type={type}
                placeholder=""
                onChange={(event) => onChange(name, event.target.value)}
            />
        </label>
    </>
}
