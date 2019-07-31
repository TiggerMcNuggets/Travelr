import Repository from "./Repository";

export const getUserAlbums = async userId => {
  return Repository.get(`/users/${userId}/albums`);
};

export const getSingleImage = async (userId, filename) => {
  return Repository.get(`/users/${userId}/media/${filename}`);
};

export const updateMedia = async (userId, mediaId, payload) => {
  return Repository.post(`/users/${userId}/media/${mediaId}`, payload);
};

export const deleteMedia = async (userId, albumId, mediaId, deleteAll) => {
  const deleteAllQuery = deleteAll ? "?removeAll=1" : "?removeAll=0";
  return Repository.delete(
    `/users/${userId}/albums/${albumId}/media/${mediaId}${deleteAllQuery}`
  );
};

export const moveMediaToAlbum = async (userId, albumId, mediaId) => {
  return Repository.put(`/users/${userId}/albums/${albumId}/media/${mediaId}`);
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

export const updateAlbum = async (userId, albumId, payload) => {
  return Repository.put(`/users/${userId}/albums/${albumId}`, payload);
};

export const updateMediaAlbums = async (userId, mediaId, payload) => {
  return Repository.put(`/users/${userId}/media/${mediaId}`, payload);
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
  createAlbum,
  updateAlbum,
  updateMediaAlbums
};
