import {useEffect, useState} from "react";
import {getAllAdoptionForms} from "../services/adoptionformService.js";
import {AdoptionCard} from "../components/AdoptionCard.jsx";

export function MyAdoptionFormsPage() {
    const [adoptionForms, setAdoptionForms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        getAllAdoptionForms()
            .then(adoptionForms => setAdoptionForms(adoptionForms))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, []);

    if (error) return <p>Could not load adoption forms: {error}</p>;

    if (loading) return <p>Loading...</p>;

    return (
        <>
            <h1>All Adoption Forms</h1>
            {adoptionForms.length ? (
                adoptionForms.map((adoption) => (
                    <AdoptionCard id={adoption.id} adoption={adoption} />
                ))
            ) : (
                <p>There are currently no applications</p>
            )}
        </>
    );
}
