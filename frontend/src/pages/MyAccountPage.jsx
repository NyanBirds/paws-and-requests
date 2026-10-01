import {Box} from "../components/Box.jsx";
import {HiOutlineClipboardDocumentList} from "react-icons/hi2";
import {LuDog} from "react-icons/lu";
import {PiCat} from "react-icons/pi";
import {MdOutlinePostAdd} from "react-icons/md";
import {useEffect, useState} from "react";
import {fetchMe} from "../services/authService.js";
import {useNavigate} from "react-router";
import {FaUser} from "react-icons/fa";
import {CiEdit} from "react-icons/ci";
import styles from "./MyAccountPage.module.css";

export function MyAccountPage() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchMe()
            .then(response => setUser(response))
            .catch(error => setError(error.message));
    }, [])

    if (error) return <p>Could not load user: {error}</p>;

    if (!user) return <p>Loading...</p>;

    return (
        <div className={styles.page}>
            <h1>Welcome, {user.firstName}!</h1>
            <Box>
                <div className={styles.accountInfo}>
                    <FaUser size="100"/>
                    <div className={styles.accountDetails}>
                        <h3>Account Information</h3>
                        <p>Name: {user.firstName} {user.lastName}</p>
                        <p>Email: {user.email}</p>
                        <p>Phone Nr: {user.phoneNumber}</p>
                        <CiEdit
                            className={styles.editIcon}
                            size="30"
                            onClick={() => navigate('/me/edit')}
                        />
                    </div>
                </div>
            </Box>
            {user.role === "SHELTERUSER" || user.role === "ADMIN" ? (
            <div className={styles.cardRow}>
                <Box
                    onClick={() => navigate("/me/animals")}
                >
                    <div>
                        <LuDog size="40"/>
                        <PiCat size="40"/>
                    </div>
                    <h2>All Shelter Animals</h2>
                    <p>View all registered animals in your shelter</p>
                </Box>
                <Box
                    onClick={() => navigate("/me/posts")}
                >
                    <MdOutlinePostAdd size="40"/>
                    <h2>All Posts</h2>
                    <p>View all your posts across all animals in your shelter</p>
                </Box>
                <Box
                    onClick={() => navigate("/me/adoptionForms")}
                >
                    <HiOutlineClipboardDocumentList size="40"/>
                    <h2>All Adoption Applications</h2>
                    <p>View all adoption applications across all active posts</p>
                </Box>
            </div>
            ) : (
                <div className={styles.singleCard}>
                    <Box
                        onClick={() => navigate("/me/adoptionForms")}
                    >
                        <HiOutlineClipboardDocumentList size="40"/>
                        <h2>All My Sent Applications</h2>
                        <p>View all adoption applications you have sent across all animals</p>
                    </Box>
                </div>
            )}
        </div>
    );
}
