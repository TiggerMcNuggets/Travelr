import Repository from "./Repository";

export const getUserAlbums = async (userId) => {
  return Repository.get(`/users/${userId}/albums`);
};

export const getSingleImage = async (userId, filename) => {
  return Repository.get(`/users/${userId}/media/${filename}`);
};

export const updateMedia = async (userId, mediaId,payload) => {
  return Repository.post(`/users/${userId}/media/${mediaId}`, payload);
};

export const deleteMedia = async (userId, albumId, mediaId) => {
  return Repository.delete(`/users/${userId}/albums${albumId}/media/${mediaId}`);
};

export const moveMediaToAlbum = async (userId, albumId, mediaId, payload) => {
  return Repository.patch(`/users/${userId}/albums${albumId}/media/${mediaId}`, payload);
};

export const deleteAlbum = async (userId, albumId) => {
  return Repository.delete(`/users/${userId}/albums/${albumId}`);
};

export const getAlbumContent = async (userId, albumId) => {
  return Repository.get(`/users/${userId}/albums/${albumId}`);
};

export const uploadMediaToAlbum = async (userId, albumId, payload) => {
  return Repository.post(`/users/${userId}/albums/${albumId}`, payload);
};

export const createAlbum = async (userId, payload) => {
  return Repository.post(`/users/${userId}/albums`, payload);
};

export default {
  getUserAlbums,
  getSingleImage,
  updateMedia,
  deleteMedia,
  moveMediaToAlbum,
  deleteAlbum,
  getAlbumContent,
  uploadMediaToAlbum,
  createAlbum
};