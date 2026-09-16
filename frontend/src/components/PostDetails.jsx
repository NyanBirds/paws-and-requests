export default function PostDetails({title, age, gender, species}) {

    return(
        <div>
            <h2>Post</h2>
            <h3>{title ?? "N/A"}</h3>
            <h3>{age ?? "N/A"}</h3>
            <h3>{gender ?? "N/A"}</h3>
            <h3>{species ?? "N/A"}</h3>
        </div>
    );
}
