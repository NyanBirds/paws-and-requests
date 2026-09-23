import {api} from "../api/client.js";

export const getAnimals = () => api.get('/animals');
