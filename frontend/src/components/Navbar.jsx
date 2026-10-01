import {useState} from "react";
import {Link, NavLink} from "react-router";
import logo from "../assets/logo_transparent.png";
import styles from "./Navbar.module.css";

function linkClass({ isActive }) {
    return isActive ? `${styles.navLink} ${styles.activeLink}` : styles.navLink;
}

export default function Navbar({ isLoggedIn, user, onLogout }) {
    const [menuOpen, setMenuOpen] = useState(false);
    const close = () => setMenuOpen(false);

    const role = user?.role;
    const initial = user?.firstName?.charAt(0).toUpperCase();

    return (
        <header className={styles.navbar}>
            <Link to="/" className={styles.logoLink} onClick={close}>
                <img src={logo} alt="" className={styles.logoImage}/>
                <span className={styles.siteName}>Paws and Requests</span>
            </Link>

            <button
                type="button"
                className={styles.menuToggle}
                aria-label="Menu"
                aria-expanded={menuOpen}
                aria-controls="main-menu"
                onClick={() => setMenuOpen(!menuOpen)}
            >
                <span className={styles.menuIconLine}/>
                <span className={styles.menuIconLine}/>
                <span className={styles.menuIconLine}/>
            </button>

            <nav
                id="main-menu"
                className={`${styles.navLinks} ${menuOpen ? styles.navLinksOpen : ""}`}
            >
                <NavLink to="/posts" className={linkClass} onClick={close}>
                    Animals
                </NavLink>
                <NavLink to="/shelters" className={linkClass} onClick={close}>
                    Shelters
                </NavLink>
                {role === "SHELTERUSER" && (
                    <NavLink to="/post/new" className={linkClass} onClick={close}>
                        Create post
                    </NavLink>
                )}

                <span className={styles.separator} aria-hidden="true"/>

                {isLoggedIn ? (
                    <>
                        <NavLink to="/me" className={styles.accountLink} onClick={close}>
                            <span className={styles.userInitial} aria-hidden="true">
                                {initial || "?"}
                            </span>
                            {user?.firstName ? `Hello, ${user.firstName}` : "My account"}
                        </NavLink>
                        <button
                            type="button"
                            className={styles.logoutButton}
                            onClick={() => { close(); onLogout(); }}
                        >
                            Log out
                        </button>
                    </>
                ) : (
                    <>
                        <NavLink to="/login" className={linkClass} onClick={close}>
                            Log in
                        </NavLink>
                        <NavLink to="/registration" className={styles.signUpButton} onClick={close}>
                            Sign up
                        </NavLink>
                    </>
                )}
            </nav>
        </header>
    );
}
