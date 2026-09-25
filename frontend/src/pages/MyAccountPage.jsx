import {Box} from "../components/Box.jsx";
import {HiOutlineClipboardDocumentList} from "react-icons/hi2";
import {LuDog} from "react-icons/lu";
import {PiCat} from "react-icons/pi";
import {MdOutlinePostAdd} from "react-icons/md";

export function MyAccountPage() {

    return (
        <div style={{ display: 'flex', flexDirection: 'row' }}>
            <Box width="25%">
                <div>
                    <LuDog size="40"/>
                    <PiCat size="40"/>
                </div>
                <h2>All Shelter Animals</h2>
                <p>View all registered animals in your shelter</p>
            </Box>
            <Box width="25%">
                <MdOutlinePostAdd size="40"/>
                <h2>All Posts</h2>
                <p>View all your posts across all animals in your shelter</p>
            </Box>
            <Box width="25%">
                <HiOutlineClipboardDocumentList size="40"/>
                <h2>All Adoption Applications</h2>
                <p>View all adoption applications across all active posts</p>
            </Box>
        </div>
    );
}
