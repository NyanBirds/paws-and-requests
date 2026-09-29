import {useEffect, useState} from "react";
import {getAllAdoptionForms} from "../services/adoptionformService.js";
import {Box} from "../components/Box.jsx";
import {getAnimals} from "../services/animalService.js";

export function MyAdoptionFormsPage() {
    const [adoptionForms, setAdoptionForms] = useState([]);
    const [animals, setAnimals] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        getAllAdoptionForms()
            .then(adoptionForms => setAdoptionForms(adoptionForms))
            .catch(error => setError(error.message));

        getAnimals()
            .then(data => setAnimals(data));
    }, []);

    const animalMap = Object.fromEntries(
        animals.map((animal) => [animal.id, animal])
    )

    if (error) return <p>Could not load adoption forms: {error}</p>;

    if (!adoptionForms.length) return <p>Loading...</p>;

    return (
        <>
            <h1>All Adoption Forms</h1>
            {adoptionForms.length ? (
                adoptionForms.map((adoption) => (
                    <Box width="35%">
                        <p><strong>Applicant:</strong> {adoption.firstName} {adoption.lastName}</p>
                        <p><em>{adoption.email}</em></p>
                        <p><strong>Application Text:</strong> {adoption.content}</p>
                        <p><strong>Animal:</strong> {animalMap[adoption.animalId]?.name} ({animalMap[adoption.animalId]?.species})</p>
                    </Box>
                ))
            ) : (
                <p>There are currently no applications</p>
            )}
        </>
    );
}
