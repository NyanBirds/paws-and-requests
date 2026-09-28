import {Box} from "../components/Box.jsx";
import {HiOutlineClipboardDocumentList} from "react-icons/hi2";
import {LuDog} from "react-icons/lu";
import {PiCat} from "react-icons/pi";
import {MdOutlinePostAdd} from "react-icons/md";
import {useEffect, useState} from "react";
import {fetchMe} from "../services/authService.js";
import {useNavigate} from "react-router";

export function MyAccountPage() {
    const navigate = useNavigate();
    const [role, setRole] = useState(null);
    useEffect(() => {
        fetchMe()
            .then(user => setRole(user.role));
    }, [])

    return (
        <>
            <h1>Welcome</h1>
            {role === "SHELTERUSER" ? (
            <div style={{ display: 'flex', flexDirection: 'row' }}>
                <Box
                    width="25%"
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
                    width="25%"
                    onClick={() => navigate("/me/posts")}
                >
                    <MdOutlinePostAdd size="40"/>
                    <h2>All Posts</h2>
                    <p>View all your posts across all animals in your shelter</p>
                </Box>
                <Box
                    width="25%"
                    onClick={() => navigate("/me/adoptionForms")}
                >
                    <HiOutlineClipboardDocumentList size="40"/>
                    <h2>All Adoption Applications</h2>
                    <p>View all adoption applications across all active posts</p>
                </Box>
            </div>
            ) : (
                <Box
                    width="25%"
                >
                    <HiOutlineClipboardDocumentList size="40"/>
                    <h2>All My Sent Applications</h2>
                    <p>View all adoption applications you have sent across all animals</p>
                </Box>
            )}
        </>
    );
}
