import { Link } from "react-router";
import { useState } from "react";
import { BASE_URL } from "../api/client";

export default function ShelterCard( {orgNr, shelterName, address, description, url} ) {
    const [hovered, setHovered] = useState(false);

    return (
        <Link
            to={`/shelters/${orgNr}`}
            onMouseEnter={() => setHovered(true)}
            onMouseLeave={() => setHovered(false)}
            style={{
                display: "block",
                textDecoration: "none",
                color: "inherit",
                flex: "0 1 320px",
                boxSizing: "border-box",
            }}
        >
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    background: "var(--bg)",
                    border: "1px solid var(--border)",
                    borderRadius: "8px",
                    padding: "24px",
                    boxSizing: "border-box",
                    height: "100%",
                    textAlign: "left",
                    boxShadow: hovered ? "0 8px 24px rgba(0, 0, 0, 0.15)" : "0 2px 8px rgba(0, 0, 0, 0.1)",
                    transition: "box-shadow 0.25s",
                }}
            >
                <img
                    src={`${BASE_URL}${url}`}
                    alt={shelterName}
                    style={{
                        display: "block",
                        width: "auto",
                        maxWidth: "100%",
                        height: "140px",
                        objectFit: "contain",
                        margin: "0 auto 12px",
                    }}
                />
                <h3 style={{ margin: "0 0 6px", color: "var(--text-h)", fontSize: "1.1rem" }}>{shelterName}</h3>
                <p style={{ margin: "0 0 4px", color: "var(--text)" }}>{address}</p>
                <p style={{ margin: 0, color: "var(--text)" }}>{description}</p>
            </div>
        </Link>
    )
}