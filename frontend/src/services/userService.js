import {api} from "../api/client.js";

export const editUser = (id, userData) => api.patch(`/api/users/${id}`, userData)
