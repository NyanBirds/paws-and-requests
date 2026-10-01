import {useEffect, useState} from "react";
import {addAnimal, getAnimals} from "../services/animalService.js";
import {Popup} from "../components/Popup.jsx";
import InputField from "../components/InputField.jsx";
import DropDown from "../components/DropDown.jsx";
import {AnimalCard} from "../components/AnimalCard.jsx";
import "../components/Button.css";

export function MyAnimalsPage() {
    const [animals, setAnimals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        getAnimals()
            .then(data => setAnimals(data))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, []);

    const [popupIsOpen, setPopupIsOpen] = useState(false);
    const [animalData, setAnimalData] = useState({});

    const onChange = (key, value) => {
        setAnimalData({ ...animalData, [key]: value });
    }

    const [status, setStatus] = useState("idle");
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setStatus('submitting');

        addAnimal(animalData)
            .then(() => {
                setStatus("success");
                setPopupIsOpen(false);
                window.location.reload();
            }, (error) => {
                setStatus("error");
                setMessage(error.message);
            });
    }

    if (error) return <p>Could not load animals: {error}</p>;

    if (loading) return <p>Loading...</p>;

    return (
        <div>
            <h1>All Shelter Animals</h1>
            <button
                className="btn btn-primary"
                style={{ marginBottom: "24px" }}
                onClick={ () => setPopupIsOpen(true) }
            >
                Add New Animal
            </button>
            {animals.length ? (
                <div style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                    gap: "20px",
                    padding: "20px",
                    width: "100%",
                    boxSizing: "border-box"}}>
                    {animals.map((animal) => (
                        <AnimalCard
                            id={animal.id}
                            name={animal.name}
                            age={animal.age}
                            gender={animal.gender}
                            species={animal.species}
                        />
                    ))}
                </div>
            ) : (
                <p>No animals yet</p>
            )}


            <Popup
                isOpen={popupIsOpen}
                onClose={() => setPopupIsOpen(false)}
                title="Animal Information"
            >
                <form onSubmit={handleSubmit}>
                    <InputField
                        name="name"
                        label="Name"
                        onChange={onChange}
                    />
                    <InputField
                        name="age"
                        type="number"
                        label="Age"
                        onChange={onChange}
                    />
                    <DropDown
                        name="gender"
                        items={[{id: "MALE", name: "Male"}, {id: "FEMALE", name: "Female"}]}
                        label="Gender"
                        onChange={onChange}
                    />
                    <DropDown
                        name="species"
                        items={[{id: "DOG", name: "Dog"}, {id: "CAT", name: "Cat"}]}
                        label="Species"
                        onChange={onChange}
                    />
                    <button type="submit" className="btn btn-primary" disabled={status === "submitting"}>
                        {status === "submitting" ? "Adding Animal..." : "Add Animal"}
                    </button>
                </form>
                {status === "error" && <p>{message}</p>}
            </Popup>
        </div>
    );
}
