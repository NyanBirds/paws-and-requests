import {useEffect, useState} from "react";
import PostDetails from "../components/PostDetails.jsx";
import {Box} from "../components/Box.jsx";
import {Checkbox} from "../components/Checkbox.jsx";
import {getPosts} from "../api/pages.js";

export default function PostsPage() {

    const [posts, setPosts] = useState([]);
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
    }, [checkboxes]);

    const checkboxChange = (key, checked) => {
        setCheckboxes({ ...checkboxes, [key]: checked });
    };

    return(
        <div>
            <h1>Look at all these cuties!</h1>
            <div style={{ display: 'flex', gap: '10rem' }}>
                <div>
                    {filters.map(filter => (
                        <Checkbox
                            key={filter.value}
                            id={filter.value}
                            checked={ checkboxes[filter.value] }
                            onChange={checkboxChange}
                        />
                    ))}
                </div>
                <ul>
                    {posts.map(post => <PostDetails key={post.id} {...post}/>)}
                </ul>
            </div>
        </div>
    );
}
