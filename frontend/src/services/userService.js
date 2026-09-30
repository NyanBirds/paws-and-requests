import {api} from "../api/client.js";

export const editUser = (id, userData) => api.patch(`/users/${id}`, userData)
