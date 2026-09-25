export function Box({ children }) {

    return (
        <div
            style={{
                maxWidth: "400px",
                margin: "40px auto",
                padding: "24px",
                border: "1px solid #ddd",
                borderRadius: "8px",
                boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            }}
        >
            {children}
        </div>
    );
}
