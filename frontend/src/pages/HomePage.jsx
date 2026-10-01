import {useEffect, useState} from "react";
import {Link, useOutletContext} from "react-router";
import styles from "./HomePage.module.css"
import ShelterCarousel from "../components/ShelterCarousel.jsx";
import PostCard from "../components/PostCard.jsx";
import {getPosts} from "../services/postService.js";

export default function HomePage() {
    const { user } = useOutletContext();
    const [latestPosts, setLatestPosts] = useState([]);

    useEffect(() => {
        getPosts("")
            .then(posts => setLatestPosts(posts.slice(-3).reverse()))
            .catch(error => console.log(error.message));
    }, []);

    const isShelter = user?.role === "SHELTERUSER";

    return <>
        <section className={styles.titleSection}>
            <h1>Paws and Requests</h1>
            <p className={styles.subtitle}>Finding homes for every paw</p>
        </section>

        <section className={styles.shelterSection}>
            <h2 className={styles.sectionHeading}>Our shelters</h2>
            <ShelterCarousel/>
            <Link to="/shelters" className={styles.arrowLink}>
                See all shelters
            </Link>
        </section>

        <section className={styles.twoColumns}>
            <div className={styles.column}>
                <h2>Looking for a new best friend?</h2>
                <p>
                    Browse the animals that are up for adoption right now
                </p>
                <div className={styles.columnActions}>
                    <Link to="/posts" className={styles.actionButton}>
                        View all animals
                    </Link>
                </div>
            </div>

            <div className={`${styles.column} ${styles.rightColumn}`}>
                <h2>Representing a shelter?</h2>
                {isShelter ? (
                    <>
                        <p>
                            Share the animals in your care and manage the
                            adoption requests you receive.
                        </p>
                        <div className={styles.columnActions}>
                          <Link to="/post/new" className={styles.actionButton}>
                                Create a post
                          </Link>
                            <Link to="/me/adoptionForms" className={styles.arrowLink}>
                                See adoption requests
                            </Link>
                        </div>
                    </>
                ) : (
                    <>
                        <p>
                            Register your shelter to post animals for adoption
                            and receive requests from people who want to adopt.
                        </p>
                        <div className={styles.columnActions}>
                            <Link
                                to="/registration"
                                state={{ shelter: true }}
                                className={styles.actionButton}
                            >
                                Register your shelter
                            </Link>
                            <Link to="/login" className={styles.arrowLink}>
                                Already registered? Log in
                            </Link>
                        </div>
                    </>
                )}
            </div>
        </section>
    </>
}
