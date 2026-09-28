import {useEffect, useState} from "react";
import {getAnimals} from "../services/animalService.js";

export function MyAnimalsPage() {
    const [animals, setAnimals] = useState([]);

    useEffect(() => {
        getAnimals()
            .then(data => setAnimals(data));
    })

    return (
        <>
            <h1>All Shelter Animals</h1>
            {animals.map((animal) => (
                <h2>{animal.name}</h2>
            ))}
        </>
    );
}
