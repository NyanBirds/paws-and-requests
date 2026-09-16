import { Link } from "react-router";

export default function PostDetails({postId, title, age, gender, species}) {
    return(
        <Link to={`/posts/${postId}`}>
            <div>
                <h2>{title ?? "N/A"}</h2>
                <h3>Age: {age ?? "N/A"}</h3>
                <h3>Gender: {gender ?? "N/A"}</h3>
                <h3>Species: {species ?? "N/A"}</h3>
            </div>
        </Link>
    );
}
