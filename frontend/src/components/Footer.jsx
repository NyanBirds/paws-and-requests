import {Link} from "react-router";
import logo from "../assets/logo_transparent.png";
import styles from "./Footer.module.css";

export default function Footer() {
    const year = new Date().getFullYear();

    return (
        <footer className={styles.footer}>
            <div className={styles.footerColumns}>
                <div className={styles.aboutColumn}>
                    <Link to="/" className={styles.logoLink}>
                        <img src={logo} alt="" className={styles.logoImage}/>
                        <span>Paws and Requests</span>
                    </Link>
                    <p>Finding homes for every paw.</p>
                </div>

                <nav className={styles.linkColumn} aria-label="Adopt">
                    <h3 className={styles.columnHeading}>Adopt</h3>
                    <Link to="/posts">Animals for adoption</Link>
                    <Link to="/shelters">Shelters</Link>
                </nav>

                <nav className={styles.linkColumn} aria-label="For shelters">
                    <h3 className={styles.columnHeading}>For shelters</h3>
                    <Link to="/registration" state={{ shelter: true }}>
                        Register your shelter
                    </Link>
                    <Link to="/login">Log in</Link>
                </nav>
            </div>

            <p className={styles.copyright}>
                © {year} Paws and Requests · Made by NyanBirds
            </p>
        </footer>
    );
}
