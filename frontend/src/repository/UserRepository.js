import Repository from "./Repository";

export default {
  /**
   * Gets users based on filter parameters
   * @param {*} params Filter parameters to search up users
   */
  getUsers(params) {
    return Repository.get("/travellers_filter", {
      params: params
    });
  },

  /**
   * Gets a user given their id
   * @param {Number} id The user id
   */
  getUser(id) {
    return Repository.get(`/travellers/${id}`);
  },

  /**
   * Creates a user given valid user data.
   * @param {Object} payload New user data
   */
  createUser(payload) {
    return Repository.post("/travellers", payload);
  },

  /**
   * Updates a users profile information with a given id and valid data.
   * @param {Number} id The users id
   * @param {Object} payload The updated user data.
   */
  updateUser(id, payload) {
    return Repository.put(`/travellers/${id}`, payload);
  },

  /**
   * Gets all traveller types (DUPLICATE x3!!!!)
   */
  getTravellerTypes() {
    return Repository.get(`/traveller-types`);
  },

  /**
   * Gets details of the authenticated user.
   */
  getMe() {
    return Repository.get("/travellers/me");
  },

  /**
   * Gets all nationalities (DUPLICATE x3!!!!!!)
   */
  getNationalities() {
    return Repository.get(`/nationalities`);
  },

  /**
   * Undo operation for user deletion
   * @param {Number} userId The users id
   */
  toggleUserDeleted(userId) {
    return Repository.put(`/travellers/${userId}/toggle_deleted`);
  },

  // Sets the user profile picture to a selected photo
  setProfilePic(id, payload) {
    return Repository.put(`/travellers/${id}/setphoto`, payload);
  },

  // Refer to:  https://a.slack-edge.com/1ab09/img/api/slack_oauth_flow_diagram@2x.png
  slackOAuthStep3(id, payload) {
    return Repository.post(`/travellers/${id}/slackRequestAuth`, payload);
  },

  createSlackChannel(id, payload) {
    return Repository.post(`/travellers/${id}/slackPrivateChannel`, payload);
  }
};