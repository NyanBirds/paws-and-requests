import {Navigate, Outlet, useOutletContext} from "react-router";

export default function ProtectedRoute() {
    const context = useOutletContext();

    if (!context.isLoggedIn) {
        return <Navigate to="/login" replace />;
    }

    return <Outlet context={context} />;
}