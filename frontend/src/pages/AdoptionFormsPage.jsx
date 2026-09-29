import {useEffect, useState} from "react";
import {getAdoptionForms} from "../services/adoptionformService.js";
import {useParams} from "react-router";
import {Box} from "../components/Box.jsx";

export function AdoptionFormPage() {
    const { postId } = useParams();
    const [adoptionForms, setAdoptionForms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        getAdoptionForms(postId)
            .then((adoptionForms) => setAdoptionForms(adoptionForms))
            .catch((error) => setError(error.message))
            .finally(() => setLoading(false));
    }, [postId]);

    if (error) return <p>Could not load adoption forms: {error.message}</p>;

    if (loading) return <p>Loading...</p>;

    return (
        <>
            <h1>Applications</h1>
            {adoptionForms.length ? (
                adoptionForms.map((adoption) => (
                    <Box width="35%">
                        <p><strong>Applicant:</strong> {adoption.firstName} {adoption.lastName}</p>
                        <p><em>{adoption.email}</em></p>
                        <p><strong>Application Text:</strong> {adoption.content}</p>
                    </Box>
                ))
            ) : (
                <p>No adoption forms yet</p>
            )}
        </>
    );
}
