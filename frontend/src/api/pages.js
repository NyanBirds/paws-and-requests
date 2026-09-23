import { api } from './client';

export const animalProfile = (postId) => api.get(`/posts/${postId}`);

export const adopt = (postId, content) => api.post(`/posts/${postId}/adoption`, content);

export const getAnimals = () => api.get(`/animals`);

export const newPost = (postFields, pictureFiles = []) => {
    const formData = new FormData();
    formData.append(
        "post",
        new Blob([JSON.stringify(postFields)], { 
            type: "application/json"})
    );
    pictureFiles.forEach((file) => formData.append("pictures", file));
    return api.postForm(`/posts`, formData);
};