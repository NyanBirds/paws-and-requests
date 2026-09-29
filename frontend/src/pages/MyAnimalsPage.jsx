import {useEffect, useState} from "react";
import {addAnimal, getAnimals} from "../services/animalService.js";
import {Popup} from "../components/Popup.jsx";
import InputField from "../components/InputField.jsx";
import DropDown from "../components/DropDown.jsx";

export function MyAnimalsPage() {
    const [animals, setAnimals] = useState([]);

    useEffect(() => {
        getAnimals()
            .then(data => setAnimals(data));
    }, []);

    const [popupIsOpen, setPopupIsOpen] = useState(false);
    const [animalData, setAnimalData] = useState({});

    const onChange = (key, value) => {
        setAnimalData({ ...animalData, [key]: value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        addAnimal(animalData)
            .then(() => {
                setPopupIsOpen(false);
                window.location.reload();
            });
    }

    return (
        <>
            <h1>All Shelter Animals</h1>
            <button onClick={ () => setPopupIsOpen(true) }>Add New Animal</button>
            {animals.map((animal) => (
                <h2>{animal.name}</h2>
            ))}

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
                    <button type="submit">Submit</button>
                </form>
            </Popup>
        </>
    );
}
