import {api} from "../api/client.js";

export const getAdoptionForms = (postId) => api.get(`/posts/${postId}/adoption`);

export const adopt = (postId, content) => api.post(`/posts/${postId}/adoption`, content);
