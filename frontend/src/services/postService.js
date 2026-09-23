import {api} from "../api/client.js";

export const getPost = (postId) => api.get(`/posts/${postId}`);

export const getAllPosts = () => api.get('/posts');

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
