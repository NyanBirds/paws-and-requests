import { Link } from "react-router";

export default function ShelterDetails({orgNr, name, description}) {
    return <Link to={`/shelters/${orgNr}`}>
        <p>{name}</p>
        <p>{description}</p>
    </Link>
}