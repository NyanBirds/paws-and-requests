import { api } from '../api/client.js';

export const login = (credentials) => api.post('/api/auth/login', credentials);
export const register = (userData) => api.post('/api/auth/registration', userData);
