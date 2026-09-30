import {api} from "../api/client.js";

export const getAnimals = () => api.get('/api/animals');

export const getAnimal = (id) => api.get('/api/animals/' + id);

export const addAnimal = (newAnimal) => api.post('/api/animals', newAnimal);

export const deleteAnimal = (id) => api.delete('/api/animals/' + id);
