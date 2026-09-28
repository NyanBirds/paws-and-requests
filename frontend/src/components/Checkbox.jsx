export function Checkbox( { id, label, checked, onChange } ) {

    return (
        <div>
            <input
                id={id}
                type="checkbox"
                checked={checked}
                onChange={ (e) => onChange(id, e.target.checked) }
            />
            <span>{label}</span>
        </div>
    );
}
