import Repository from "./Repository";

export default {
  /**
   * Gets all user groups
   * @param {number} userId The user id
   */
  getGroupsForUser(userId) {
    return Repository.get(`/users/${userId}/group`);
  },

  /**
   * Creates a single a user group for a user.
   * @param {number} userId The user id
   */
  createUserGroup(userId, payload) {
    return Repository.post(`/users/${userId}/group`, payload);
  },

  /**
   * Gets a single user group given a user and group id
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   */
  getSingleUserGroup(userId, groupId) {
    return Repository.get(`/users/${userId}/group/${groupId}`);
  },

  /**
   * Updates a single user group given a user and group id
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   */

  /**
   *
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   * @param {Object} payload The new group information to be updated to.
   */
  updateUserGroup(userId, groupId, payload) {
    return Repository.put(`/users/${userId}/group/${groupId}`, payload);
  },

  /**
   * Deletes a single user group given a user and group id
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   */
  deleteSingleUserGroup(userId, groupId) {
    return Repository.put(`/users/${userId}/group/${groupId}/toggle_deleted`);
  },

  /**
   * Adds a a single to a user group given a user and group id and the id of the user to add.
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   * @param {number} memberId The id of the user to add to the group
   */
  addUserToUserGroup(userId, groupId, memberId) {
    return Repository.post(
      `/users/${userId}/group/${groupId}/member/${memberId}`
    );
  },

  /**
   * Updates ownership status of a group member given a user and group id and the id of the user to update.
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   * @param {number} memberId The id of the user to update the group
   */

  /**
   * Updates ownership status of a group member given a user and group id and the id of the user to update.
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   * @param {number} memberId The id of the user to update the group
   * @param {Object} payload the new member ownership information
   */
  updateUserInUserGroup(userId, groupId, memberId, payload) {
    return Repository.put(
      `/users/${userId}/group/${groupId}/member/${memberId}`,
      payload
    );
  },

  /**
   * Toggles the deletion of a group member given a user and group id and the id of the user to remove.
   * @param {number} userId The user id
   * @param {number} groupId The user group id
   * @param {number} memberId The id of the user to remove in the group
   */
  removeUserInUserGroup(userId, groupId, memberId) {
    return Repository.put(`/users/${userId}/group/${groupId}/member/${memberId}/toggle_deleted`);
  },

  /**
   * Toggles a user's ownership leve in a group
   * @param {number} userId The user id
   * @param {number} groupId The user id
   * @param {*} memberId  The member id
   */
  togglePromoteUser(userId, groupId, memberId) {
    return Repository.put(
      `/users/${userId}/group/${groupId}/member/${memberId}/promote`
    );
  }
};
