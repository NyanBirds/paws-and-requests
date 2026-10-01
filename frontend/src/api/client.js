export const BASE_URL = "http://localhost:8080"

function getToken() {
  return localStorage.getItem("authToken");
}

class ApiError extends Error {
    constructor(status, message, body) {
        super(message);
        this.status = status;
        // The parsed body when the server sent JSON, otherwise undefined. A 400
        // from validation carries { code, message, fields }, where fields maps
        // each offending input to its message so a form can mark them
        // individually.
        this.body = body;
    }
}

// Endpoints return either a JSON error envelope or a bare string, depending on
// the handler that produced it. Parse when we can and fall back to the raw
// text, so a plain-text error still reads as its message.
function toError(status, text) {
    if (!text) {
        return new ApiError(status, status === 404 ? "Not found" : text);
    }
    try {
        const body = JSON.parse(text);
        const isEnvelope = body !== null && typeof body === "object"
            && typeof body.message === "string";
        return new ApiError(
            status,
            isEnvelope ? body.message : text,
            isEnvelope ? body : undefined,
        );
    } catch {
        return new ApiError(status, text);
    }
}

async function request(path, options = {}) {
    console.log(`${BASE_URL}${path}`)
    const res = await fetch(`${BASE_URL}${path}`, {
        headers: {
            "Content-Type": "application/json",
            ...(getToken() && { Authorization: `Bearer ${getToken()}` }),
            ...options.headers,
        },
        ...options,
    });

    if (!res.ok) {
        const text = await res.text();
        console.log(text);

        throw toError(res.status, text);
    }

    return res.status === 204 ? null : res.json();
}

export const api = {
    get: (path) => request(path),
    post: (path, body) => request(path, { method: "POST", body: JSON.stringify(body) }),
    postForm: (path, formData) => requestForm(path, formData, "POST"),
    put: (path, body) => request(path, { method: "PUT", body: JSON.stringify(body) }),
    patch: (path, body) => request(path, { method: "PATCH", body: JSON.stringify(body) }),
    delete: (path) => request(path, { method: "DELETE" }),
    patchForm: (path, formData) => requestForm(path, formData, "PATCH"),
};

async function requestForm(path, formData, method) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method,
        headers: {
            ...(getToken() && { Authorization: `Bearer ${getToken()}`}),
        },
        body: formData,
    });

if (!response.ok) {
        const text = await response.text()
        console.log(text);

        throw toError(response.status, text)
    }
    return response.status === 204 ? null : response.json();
}
