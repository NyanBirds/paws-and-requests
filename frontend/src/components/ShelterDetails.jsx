import { Link } from "react-router";
import { useState } from "react";

export default function ShelterDetails({orgNr, shelterName, description}) {
    const [hovered, setHovered] = useState(false);

    return <Link
        to={`/shelters/${orgNr}`}
        onMouseEnter={() => setHovered(true)}
        onMouseLeave={() => setHovered(false)}
        style={{
            display: "block",
            textDecoration: "none",
            background: "var(--bg)",
            border: "1px solid var(--border)",
            borderColor: hovered ? "var(--accent)" : "var(--border)",
            borderRadius: "8px",
            padding: "24px",
            flex: "0 1 calc(33.333% - 10.667px)",
            boxSizing: "border-box",
            boxShadow: hovered ? "var(--shadow)" : "none",
            transition: "border-color 0.25s, box-shadow 0.25s",
        }}
    >
        <div>
            <h2 style={{ margin: "0 0 8px", color: "var(--text-h)" }}>{shelterName}</h2>
            <p style={{ color: "var(--text)" }}>{description}</p>
        </div>
    </Link>
}