import {Navigate, Outlet, useLocation, useOutletContext} from "react-router";

export default function ProtectedRoute( { roles }) {
    const context = useOutletContext();
    const originalRoute = useLocation();

    if (!context.isLoggedIn) {
        return <Navigate to="/login" replace state={{ from: originalRoute}} />;
    }

    if (roles) {
        if (context.user === undefined) {
            return <p>Loading...</p>;
        }
        if (!roles.includes(context.user?.role)) {
            return <Navigate to="/no-access" replace />;
        }
    }

    return <Outlet context={context} />;
}