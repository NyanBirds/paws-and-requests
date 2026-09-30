import {api} from "../api/client.js";

export const getAllAdoptionForms = () => api.get('/api/adoptionforms');

export const getAdoptionForms = (postId) => api.get(`/api/posts/${postId}/adoption`);

export const adopt = (postId, content) => api.post(`/api/posts/${postId}/adoption`, content);
