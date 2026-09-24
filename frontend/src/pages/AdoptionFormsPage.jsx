import {useEffect, useState} from "react";
import {getAdoptionForms} from "../services/adoptionformService.js";
import {useParams} from "react-router";
import {Box} from "../components/Box.jsx";

export function AdoptionFormPage() {
    const { postId } = useParams();
    const [adoptionForms, setAdoptionForms] = useState([]);

    useEffect(() => {
        getAdoptionForms(postId)
            .then((adoptionForms) => setAdoptionForms(adoptionForms));
    }, []);

    return (
        <>
            <h1>Applications</h1>
            {adoptionForms.length ? (
                adoptionForms.map((adoption) => (
                    <Box>
                        <p><strong>Applicant:</strong> {adoption.firstName} {adoption.lastName}</p>
                        <p><em>{adoption.email}</em></p>
                        <p><strong>Application Text:</strong> {adoption.content}</p>
                    </Box>
                ))
            ) : (
                <p>No one wants to adopt yet :(</p>
            )}
        </>
    );
}
