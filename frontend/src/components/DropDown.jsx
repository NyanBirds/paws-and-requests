export default function DropDown({className, name, items = [], label, required = true, onChange}) {
    return <div className={className}>
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
