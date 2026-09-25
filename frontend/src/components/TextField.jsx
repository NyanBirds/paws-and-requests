import styles from "./Textfield.module.css"

export default function TextField({name, label, required = true, onChange, placeholder}) {
    return <div>
        <label>{label}</label>
        <textarea className={styles}
            name={name}
            required={required}
            onChange={(event) => onChange(name, event.target.value)}
                  placeholder={placeholder}
            rows={10}
            cols={50}
        />
    </div>
}
