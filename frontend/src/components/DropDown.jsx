export default function DropDown({name, animals = [], label, required = true, onChange}) {
    return <div>
        <label>{label}</label>
        <select
            required={required}
            onChange={(event) => {
                onChange(name, event.target.value)
            }}>
            
            <option value="">---</option>
            {animals.map((animal, i) => {
                return <option key={i} value={animal.id}>{animal.name}</option>
            })}

        </select>
    </div>
}