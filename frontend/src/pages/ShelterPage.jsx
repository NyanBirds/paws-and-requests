import { useEffect, useState } from "react";
import { api } from "../api/client";
import { useParams } from "react-router";

export default function ShelterPage() {
    const { orgNr } = useParams()
    const [shelter, setShelter] = useState()
    const [error, setError] = useState()

    useEffect(() => {
        api.get(`/shelters/${orgNr}`)
            .then(res => setShelter(res), 
                  err => setError(err))
    }, [])

    return <>
    { error ? (
        <p>{error.status}</p>
    ) : (
        <div>
            <h1>{shelter?.shelterName}</h1>
            <p>{shelter?.description}</p>
            <p>{shelter?.address}</p>
            
        </div>
    )}
    </>
}