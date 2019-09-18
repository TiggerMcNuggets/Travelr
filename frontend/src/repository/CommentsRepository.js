import Repository from "./Repository";

export default {
  /**
   * Makes a comment on a trip for a particular user.
   * @param {Number} userId The user id which is posting the comment
   * @param {Number} tripId The trip id which the comment is for
   */
  postComment(userId, tripId, payload) {
    return Repository.post(
      `/users/${userId}/trips/${tripId}/comments`,
      payload
    );
  },

  /**
   * Updates a comment on a trip for a particular user.
   * @param {Number} userId The user id which is posting the comment
   * @param {Number} tripId The trip id which the comment is for
   * @param {Number} commentId The comment id
   */
  updateComment(userId, tripId, commentId, payload) {
    return Repository.put(
      `/users/${userId}/trips/${tripId}/comments/${commentId}`,
      payload
    );
  },

  /**
   * Deletes a comment on a trip for a particular user.
   * @param {Number} userId The user id which is posting the comment
   * @param {Number} tripId The trip id which the comment is for
   * @param {Number} commentId The comment id
   */
  deleteComment(userId, tripId, commentId) {
    return Repository.put(
      `/users/${userId}/trips/${tripId}/comments/${commentId}/toggle_deleted`
    );
  },

  /**
   * Toggles an emoji for a comment on a trip for a particular user.
   * @param {Number} userId The user id which is posting the comment
   * @param {Number} tripId The trip id which the comment is for
   * @param {Number} commentId The comment id
   */
  toggleEmoji(userId, tripId, commentId, payload) {
    return Repository.put(
      `/users/${userId}/trips/${tripId}/comments/${commentId}/emoji`,
      payload
    );
  }
};