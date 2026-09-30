import { api } from '../api/client.js';

export const login = (credentials) => api.post('/auth/login', credentials);

export const register = (userData) => api.post('/auth/registration', userData);

export const fetchMe = () => api.get('/auth/me');
