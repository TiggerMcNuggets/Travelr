import Repository from "./Repository";

export default {
  /**
   * Uploads a file to the server
   * @param {*} userId 
   * @param {*} tripId 
   * @param {*} files 
   */
  uploadFiles(userId, tripId, files) {
    return Repository.post(`/users/${userId}}/trips/${tripId}/files`, files);
  },

  /**
   * Get all files for a trip node
   * @param {*} userId 
   * @param {*} tripId 
   */
  getFilesForTrip(userId, tripId) {
    return Repository.get(`/users/${userId}}/trips/${tripId}/files`);
  },

  /**
   * Delete a file
   * @param {*} userId 
   * @param {*} tripId 
   * @param {*} fileId 
   */
  deleteFile(userId, tripId, fileId) {
    return Repository.delete(`/users/${userId}}/trips/${tripId}/files/${fileId}`);
  },

  /**
   * Returns the file from the server
   * @param {*} userId 
   * @param {*} fileId 
   */
  getFileURL(userId, fileId) {
    return Repository.get(`/api/users/${userId}/file/${fileId}`);
  }
}
