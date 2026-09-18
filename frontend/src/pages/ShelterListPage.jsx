import { useEffect, useState } from "react";
import { api } from "../api/client";
import ShelterDetails from "../components/ShelterDetails";
import ShelterCard from "../components/ShelterCard";
import CardGrid from "../components/CardGrid";

export default function ShelterListPage() {
    const [shelters, setShelters] = useState([])

    useEffect(() => {
        api.get(`/shelters`)
            .then(res => setShelters(res))
    }, [])

    return (
        <div>
            <h1>Shelters</h1>
            <CardGrid>
                {shelters.map(shelter => <ShelterCard key={shelter.orgNr} {...shelter}/>)}
            </CardGrid>
        </div>
    )
}