import {useState} from "react";
import {useParams} from "react-router";
import {adopt} from "../services/adoptionformService.js";
import TextField from "../components/TextField.jsx";

export function AdoptionPage() {
    const { postId } = useParams();
    const [content, setContent] = useState('');
    const [isSent, setIsSent] = useState(false);

    function onSubmit(event) {
        event.preventDefault();
        adopt(postId, {"content": content})
            .then(() => {
                setIsSent(true);
            }, (error) => {
                console.log(error.message);
            });
    }

    return (
        <div>
            {!isSent ? (
                <>
                    <h3>Adoption Form</h3>
                    <TextField
                        id="content"
                        value={content}
                        onChange={(name, value) => setContent(value)}
                        placeholder="Please tell us about yourself!"
                    />
                    <p><em>Character Count:</em> {content.length}</p>
                    <button type="submit" onClick={onSubmit}>Submit</button>
                </>
            ) : (
                <h2>Thank you for applying!</h2>
            )}
        </div>
    );
}
