import {useEffect, useState} from "react";
import {fetchMe} from "../services/authService.js";

export default function CheckUser(isLoggedIn) {
    const [user, setRole] = useState(null);

    useEffect(() => {
        if (!isLoggedIn) {
            setRole(null);
            return;
        }
        fetchMe()
            .then(users => setRole(users))
            .catch(() => setRole(null));
    }, [isLoggedIn])

    return user;
}