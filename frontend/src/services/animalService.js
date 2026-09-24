import {api} from "../api/client.js";

export const getAnimals = () => api.get('/animals');

export const getAnimal = (id) => api.get('/animals/' + id);
