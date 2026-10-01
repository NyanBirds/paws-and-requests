import {useEffect, useState} from "react";
import {Checkbox} from "../components/Checkbox.jsx";
import {getPosts} from "../services/postService.js";
import CardGrid from "../components/CardGrid.jsx";
import PostCard from "../components/PostCard.jsx";
import styles from "./PostsPage.module.css";

export default function PostsPage() {

    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const filters = [
        { label: "Male", type: "gender", value: "MALE" },
        { label: "Female", type: "gender", value: "FEMALE" },
        { label: "Dog", type: "species", value: "DOG" },
        { label: "Cat", type: "species", value: "CAT" }
    ];
    const [checkboxes, setCheckboxes] = useState(() =>
        Object.fromEntries(filters.map(filter => [filter.value, true]))
    );

    useEffect(() => {
        const gender = filters
            .filter(f => f.type === "gender" && checkboxes[f.value])
            .map(f => f.value);

        const species = filters
            .filter(f => f.type === "species" && checkboxes[f.value])
            .map(f => f.value);

        const params = new URLSearchParams();

        gender.forEach(gender => params.append("gender", gender));
        species.forEach(species => params.append("species", species));

        getPosts(params)
            .then((res) => {
                setPosts(res);
            })
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, [checkboxes]);

    const checkboxChange = (key, checked) => {
        setCheckboxes({ ...checkboxes, [key]: checked });
    };

    if (error) return <p>Could not load posts: {error}</p>;

    if (loading) return <p>Loading...</p>

    return(
        <div className={styles.page}>
            <h1 className={styles.pageHeading}>Animals up for adoption</h1>
            <div className={styles.layout}>
                <aside className={styles.filterPanel}>
                    <p className={styles.filterTitle}>Filters</p>
                    <div className={styles.filterGroups}>
                        <fieldset className={styles.filterGroup}>
                            <legend>Gender</legend>
                            {filters.slice(0, 2).map(filter => (
                                <Checkbox
                                    key={filter.value}
                                    id={filter.value}
                                    label={filter.label}
                                    checked={ checkboxes[filter.value] }
                                    onChange={checkboxChange}
                                />
                            ))}
                        </fieldset>
                        <fieldset className={styles.filterGroup}>
                            <legend>Species</legend>
                            {filters.slice(2, 4).map(filter => (
                                <Checkbox
                                    key={filter.value}
                                    id={filter.value}
                                    label={filter.label}
                                    checked={ checkboxes[filter.value] }
                                    onChange={checkboxChange}
                                />
                            ))}
                        </fieldset>
                    </div>
                </aside>

                <div>
                    {posts.length ? (
                        <CardGrid> {posts.map(post => <PostCard key={post.id} {...post}/>)} </CardGrid>
                    ) : (
                        <p className={styles.emptyMessage}>
                            There are currently no animals up for adoption
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
}
