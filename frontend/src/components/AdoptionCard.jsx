import {Box} from "./Box.jsx";
import {deleteAdoptionForm} from "../services/adoptionformService.js";
import "./Button.css";
import styles from "./AdoptionCard.module.css";

export function AdoptionCard( {id, adoption} ) {

    const handleDelete = async (e) => {
        e.preventDefault();
        deleteAdoptionForm(id)
            .then(() => {
                window.location.reload();
            });
    }

    return (
        <div className={styles.cardWrapper}>
            <Box>
                <div className={styles.cardContent}>
                    <p><strong>Applicant:</strong> {adoption.firstName} {adoption.lastName}</p>
                    <p><em>{adoption.email}</em></p>
                    <p><strong>Application Text:</strong> {adoption.content}</p>
                    <p><strong>Animal:</strong> {adoption.animalName} ({adoption.species})</p>
                </div>
                <button
                    className={`btn btn-danger ${styles.deleteButton}`}
                    onClick={handleDelete}>Delete</button>
            </Box>
        </div>
    );
}
