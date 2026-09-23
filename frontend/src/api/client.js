export const BASE_URL = "http://localhost:8080"

function getToken() {
  return localStorage.getItem("authToken");
}

class ApiError extends Error {
    constructor(status, message) {
        super(message);
        this.status = status;
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
        const message = await res.text()
        console.log(message);
        
        throw new ApiError(res.status, message || res.statusText);
    }

    return res.status === 204 ? null : res.json();
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: "POST", body: JSON.stringify(body) }),
   postForm: (path, formData) => requestForm(path, formData),
  put: (path, body) => request(path, { method: "PUT", body: JSON.stringify(body) }),
  delete: (path) => request(path, { method: "DELETE" }),
};

async function requestForm(path, formData) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method: "POST",
        headers: {
            ...(getToken() && { Authorization: `Bearer ${getToken()}`}),
        },
        body: formData,
    });

    if (!response.ok) {
        const message = await response.text()
        console.log(message);
        throw new ApiError(response.status, message || response.statusText)
    }
    return response.status === 204 ? null : response.json();
}