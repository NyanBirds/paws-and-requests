import { api } from './client';

export const animalProfile = (postId) => api.get(`/posts/${postId}`);