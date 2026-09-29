import {api} from "../api/client.js";

export const getAnimals = () => api.get('/animals');

export const getAnimal = (id) => api.get('/animals/' + id);

export const addAnimal = (newAnimal) => api.post('/animals', newAnimal);

export const deleteAnimal = (id) => api.delete('/animals/' + id);
