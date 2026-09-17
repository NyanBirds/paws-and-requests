import { useEffect, useState } from "react";
import { api } from "../api/client";
import ShelterDetails from "../components/ShelterDetails";

export default function ShelterListPage() {
    const [shelters, setShelters] = useState([])

    useEffect(() => {
        api.get(`/shelters`)
            .then(res => setShelters(res))
    }, [])

    return <>
    {shelters.map(shelter => <ShelterDetails key={shelter.id} {...shelter}/>)}
    </>
}