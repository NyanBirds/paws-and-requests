import { api } from './client';

export const animalProfile = (postId) => api.get(`/posts/${postId}`);

export const adopt = (postId, content) => api.post(`/posts/${postId}/adoption`, content);
