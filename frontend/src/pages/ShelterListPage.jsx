import { useEffect, useState } from "react";
import { api } from "../api/client";
import ShelterCard from "../components/ShelterCard";
import CardGrid from "../components/CardGrid";

export default function ShelterListPage() {
    const [shelters, setShelters] = useState([])
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null)

    useEffect(() => {
        api.get(`/shelters`)
            .then(res => setShelters(res))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, [])

    if (error) return <p>Could not load shelters: {error}</p>;

    if (loading) return <p>Loading...</p>;

    return (
        <div>
            <h1>Shelters</h1>
            {shelters.length ? (
                <CardGrid>
                    {shelters.map(shelter => <ShelterCard key={shelter.orgNr} {...shelter}/>)}
                </CardGrid>
            ) : (
                <p>No shelters yet</p>
            )}
        </div>
    )
}
