import {deleteAnimal} from "../services/animalService.js";
import {Box} from "./Box.jsx";
import "./Button.css";

export function AnimalCard( {id, name, age, gender, species} ) {

    const handleDelete = async (e) => {
        e.preventDefault();
        deleteAnimal(id)
            .then(() => {
                window.location.reload();
            });
    }

    return (
        <Box width="80%">
            <div style={{ textAlign: "left", paddingLeft: "1rem" }}>
                <h3>{name}</h3>
                <p>{age} years old</p>
                <p>Gender: {gender}</p>
                <p>Species: {species}</p>
            </div>
            <button
                className="btn btn-danger"
                style={{ marginTop: "16px" }}
                onClick={handleDelete}
            >
                Delete
            </button>
        </Box>
    );
}
