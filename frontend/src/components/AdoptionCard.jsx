import {Box} from "./Box.jsx";
import {deleteAdoptionForm} from "../services/adoptionformService.js";

export function AdoptionCard( {id, adoption} ) {

    const handleDelete = async (e) => {
        e.preventDefault();
        deleteAdoptionForm(id)
            .then(() => {
                window.location.reload();
            });
    }

    return (
        <Box width="35%">
            <div style={{ textAlign: "left", paddingLeft: "1rem" }}>
                <p><strong>Applicant:</strong> {adoption.firstName} {adoption.lastName}</p>
                <p><em>{adoption.email}</em></p>
                <p><strong>Application Text:</strong> {adoption.content}</p>
                <p><strong>Animal:</strong> {adoption.animalName} ({adoption.species})</p>
            </div>
            <button onClick={handleDelete}>Delete</button>
        </Box>
    );
}
