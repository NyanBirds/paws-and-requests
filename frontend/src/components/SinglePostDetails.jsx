export default function SinglePostDetails({
    title, description, shelterName, shelterAddress,
    pictureUrl, age, gender, species, name} ) {

    console.log(title);

    return(
        <div>
            <h1>{title ?? "N/A"}</h1>
            <img src={pictureUrl}/>
            <h2>{name ?? "N/A"}</h2>
            <h6>Age: {age ?? "N/A"}</h6>
            <h6>Gender: {gender ?? "N/A"}</h6>
            <h6>Species: {species ?? "N/A"}</h6>
            <h6>Shelter: {shelterName ?? "N/A"}</h6>
            <h6>Address: {shelterAddress ?? "N/A"}</h6>
            <h6><strong>About</strong></h6>
            <p>{description ?? "N/A"}</p>
        </div>
    );
}
