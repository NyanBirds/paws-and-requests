export default function DropDown({name, items = [], label, required = true, onChange}) {
    return <div>
        <label>{label}</label>
        <select
            required={required}
            onChange={(event) => {
                onChange(name, event.target.value)
            }}>
            
            <option value="">---</option>
            {items.map((item, i) => {
                return <option key={i} value={item.id}>{item.name}</option>
            })}

        </select>
    </div>
}
