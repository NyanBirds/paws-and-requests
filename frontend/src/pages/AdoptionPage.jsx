import {useState} from "react";
import {useParams} from "react-router";
import {adopt} from "../services/adoptionformService.js";
import TextField from "../components/TextField.jsx";

export function AdoptionPage() {
    const { postId } = useParams();
    const [content, setContent] = useState('');
    const [isSent, setIsSent] = useState(false);

    const [status, setStatus] = useState("idle");
    const [message, setMessage] = useState('');

    function onSubmit(event) {
        event.preventDefault();
        setStatus('submitting');

        adopt(postId, {"content": content})
            .then(() => {
                setStatus("success");
                setIsSent(true);
            }, (error) => {
                setStatus("error");
                setMessage(error.message);
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
                    <button
                        className="btn btn-primary"
                        type="submit"
                        disabled={status === "submitting"}
                        onClick={onSubmit}
                    >
                        {status === "submitting" ? "Submitting..." : "Submit"}
                    </button>
                    {status === "error" && <p>{message}</p>}
                </>
            ) : (
                <h2>Thank you for applying!</h2>
            )}
        </div>
    );
}
