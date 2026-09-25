export default function CardGrid({ children }) {
    return <div
        style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "center",
            gap: "16px",
            maxWidth: "1080px",
            margin: "0 auto",
            padding: "0 24px",
        }}
    >
        {children}
    </div>
}