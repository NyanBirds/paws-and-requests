import Divider from "../components/Divider";
import dyrebeskyttelsen from "../assets/dyrebeskyttelsen.png";
import fod from "../assets/fod.png";

export default function HomePage() {
    return <>
        <div>
            <h1>Paws and Request</h1>
            <h4>Finding homes for every paw</h4>
            <Divider/>
        </div>
        <div>
            <h3>SHELTERS</h3>
            <img
                src={dyrebeskyttelsen}
                height="100"
                style={{ padding: '20px' }}
            />
            <img
                src={fod}
                height="100"
                style={{ padding: '20px' }}
            />
            <Divider/>
        </div>
        <div>
            <div>
                <h3>VIEW COLLECTION</h3>
            </div>
            <div>
                <h3>REPRESENTING A SHELTER?</h3>
            </div>
            <Divider/>
        </div>


    </>
}
