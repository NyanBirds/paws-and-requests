import {useEffect, useState} from "react";
import {Link} from "react-router";
import {api, BASE_URL} from "../api/client.js";
import styles from "./ShelterCarousel.module.css";

export default function ShelterCarousel() {
    const [shelters, setShelters] = useState([]);

    useEffect(() => {
        api.get("/shelters")
            .then(all => setShelters(all.filter(shelter => shelter.url)))
            .catch(error => console.log(error.message));
    }, []);

    if (shelters.length === 0) return null;

    const MIN_ITEMS = 12;
    const repeats = Math.ceil(MIN_ITEMS / shelters.length);
    const copy = Array.from({ length: repeats }, () => shelters).flat();

    const loop = [...copy, ...copy];
    return (
        <div className={styles.carouselWindow}>
            <div className={styles.imageStrip}>
                {loop.map((shelter, i) => (
                    <Link
                        key={i}
                        to={`/shelters/${shelter.orgNr}`}
                        className={styles.shelterLink}
                        aria-hidden={i >= shelters.length}
                        tabIndex={i >= shelters.length ? -1 : undefined}
                    >
                        <img src={`${BASE_URL}${shelter.url}`} alt={shelter.shelterName} />
                    </Link>
                ))}
            </div>
        </div>
    );
}