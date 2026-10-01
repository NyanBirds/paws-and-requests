import styles from "./Textfield.module.css"

export default function TextField({className, name, label, required = true, onChange, placeholder, defaultValue}) {
    return <div className={className}>
        <label>{label}</label>
        <textarea
            className={styles}
            name={name}
            required={required}
            onChange={(event) => onChange(name, event.target.value)}
            placeholder={placeholder}
            defaultValue={defaultValue}
            rows={10}
            cols={50}
        />
    </div>
}
