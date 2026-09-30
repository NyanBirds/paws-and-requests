import {useEffect, useState} from "react";
import {fetchMe} from "../services/authService.js";

export default function CheckUser(isLoggedIn) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        if (!isLoggedIn) {
            setUser(null);
            return;
        }
        setUser(undefined);
        fetchMe()
            .then(users => setUser(users))
            .catch(() => setUser(null));
    }, [isLoggedIn])

    return user;
}