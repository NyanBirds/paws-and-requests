import {deleteAnimal} from "../services/animalService.js";
import {Box} from "./Box.jsx";

export function AnimalCard( {name, age, gender, species} ) {

    const handleDelete = async (e) => {
        e.preventDefault();
        deleteAnimal(id)
            .then(() => {
                window.location.reload();
            });
    }

    return (
        <Box>
            <div style={{ textAlign: "left", paddingLeft: "1rem" }}>
                <h3>{name}</h3>
                <p>{age} years old</p>
                <p>Gender: {gender}</p>
                <p>Species: {species}</p>
            </div>
            <button onClick={handleDelete}>Delete</button>
        </Box>
    );
}
