import {api} from "../api/client.js";

export const getPost = (postId) => api.get(`/api/posts/${postId}`);

export const getPosts = (params) => api.get(`/api/posts?${params}`);

export const getShelterPosts = () => api.get('/api/posts/shelter');

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

export const deletePost = (postId) => api.delete(`/posts/${postId}`);

export const editPost = (postId, postFields, pictureFiles = []) => {
    const formData = new FormData();
    formData.append(
        "post",
        new Blob([JSON.stringify(postFields)], {
            type: "application/json"})
    );
    pictureFiles.forEach((file) => formData.append("pictures", file));
    return api.patchForm(`/posts/${postId}`, formData);
}

export const deletePicture = (postId, pictureId) => api.delete(`/posts/${postId}/pictures/${pictureId}`);
