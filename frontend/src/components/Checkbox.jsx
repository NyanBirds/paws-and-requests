export function Checkbox( { id, checked, onChange } ) {

    return (
        <div>
            <input
                id={id}
                type="checkbox"
                checked={checked}
                onChange={ (e) => onChange(id, e.target.checked) }
            />
            <span>{id}</span>
        </div>
    );
}
