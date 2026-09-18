import {useState} from "react";
import {adopt} from "../api/pages.js";
import {useParams} from "react-router";

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
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        rows={10}
                        cols={50}
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
