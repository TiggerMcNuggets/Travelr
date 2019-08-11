import Repository from "./Repository";

export default {
  /**
   * Gets all user groups
   * @param {*} userId The user id
   */
  getGroupsForUser(userId) {
    return Repository.get(`/users/${userId}/group`);
  },

  /**
   * Gets a single a user group for a user.
   * @param {*} userId The user id
   */
  createUserGroup(userId) {
    return Repository.post(`/users/${userId}/group`);
  },

  /**
   * Gets a single user group given a user and group id
   * @param {*} userId The user id
   * @param {*} groupId The user group id
   */
  getSingleUserGroup(userId, groupId) {
    return Repository.get(`/users/${userId}/group/${groupId}`);
  },

  /**
   * Updates a single user group given a user and group id
   * @param {*} userId The user id
   * @param {*} groupId The user group id
   */
  updateUserGroup(userId, groupId, payload) {
    return Repository.put(`/users/${userId}/group/${groupId}`, payload);
  },

  /**
   * Deletes a single user group given a user and group id
   * @param {*} userId The user id
   * @param {*} groupId The user group id
   */
  deleteSingleUserGroup(userId, groupId) {
    return Repository.delete(`/users/${userId}/group/${groupId}`);
  },

  /**
   * Adds a a single to a user group given a user and group id and the id of the user to add.
   * @param {*} userId The user id
   * @param {*} groupId The user group id
   * @param {*} memberId The id of the user to add to the group
   */
  addUserToUserGroup(userId, groupId, memberId) {
    return Repository.post(
      `/users/${userId}/group/${groupId}/member/${memberId}`
    );
  },

  /**
   * Updates ownership status of a group member given a user and group id and the id of the user to update.
   * @param {*} userId The user id
   * @param {*} groupId The user group id
   * @param {*} memberId The id of the user to update the group
   */
  updateUserInUserGroup(userId, groupId, memberId, payload) {
    return Repository.put(
      `/users/${userId}/group/${groupId}/member/${memberId}`,
      payload
    );
  },

  /**
   * Removes a group member given a user and group id and the id of the user to remove.
   * @param {*} userId The user id
   * @param {*} groupId The user group id
   * @param {*} memberId The id of the user to remove in the group
   */
  removeUserInUserGroup(userId, groupId, memberId) {
    return Repository.delete(
      `/users/${userId}/group/${groupId}/member/${memberId}`
    );
  }
};
