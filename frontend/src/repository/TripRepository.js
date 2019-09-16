import Repository from "./Repository";

export default {
  /**
   * Creates a trip for the signed in user given new trip data.
   * @param {Object} tripBody The new trip data.
   */
  createTrip(tripBody) {
    return Repository.post("/trips", tripBody);
  },

  /**
   * Creates a trip for a user based on given data
   * @param {Object} tripBody The new trip data
   * @param {Number} user_id The id of the user that the new trip will belong too.
   */
  createTripForUser(tripBody, user_id) {
    return Repository.post("/users/" + user_id + "/trips", tripBody);
  },

  /**
   * Gets a single trip for a given user from the database - same as method below - one should probably be deleted.
   * @param {Number} user_id The id of the user to get the trip for
   * @param {Number} trip_id The id of the trip to get
   */
  getUserSingleTrip(user_id, trip_id) {
    return Repository.get("users/" + user_id + "/trips/" + trip_id);
  },

  /**
   * Getting trips for the signed in user.
   */
  getTrips() {
    return Repository.get("/trips");
  },

  /**
   * Gets all of a users trips from the database
   * @param {Number} user_id The id of the user to get trips for.
   */
  getUserTrips(user_id) {
    return Repository.get("/users/" + user_id + "/trips");
  },

  /**
   * Gets a users trip from the database
   * @param {Number} userId The user id
   * @param {Number} tripId The trip id to delete.
   */
  getTrip(userId, tripId) {
    return Repository.get(`/users/${userId}/trips/${tripId}`);
  },

  /**
   * Updates a users trip with with given data.
   * @param {Number} user_id The user id
   * @param {Number} id The id of the trip to update
   * @param {Number} tripBody The new data to update the trip with
   */
  updateTrip(user_id, id, tripBody) {
    return Repository.put(`/users/${user_id}/trips/${id}`, tripBody);
  },

  /**
   * Soft deletes a users trip
   * @param {Number} userId The user id
   * @param {Number} tripId The trip id to delete.
   */
  deleteTrip(userId, tripId) {
    return Repository.put(`/users/${userId}/trips/${tripId}/toggle_deleted`);
  },

  /**
   * Downloads a users trip
   * @param {Number} userId The user id
   * @param {Number} tripId The trip id to download.
   */
  downloadTrip(userId, tripId) {
    return Repository.get(`/users/${userId}/trips/${tripId}/iCal`);
  },

  /**
   * Toggles addition/deletion of a group trip
   * @param {Number} userId The user id
   * @param {Number} tripId The trip id.
   * @param {Number} groupId The group id.
   */
  toggleGroupTrip(userId, tripId, groupId) {
    return Repository.put(`/users/${userId}/trips/${tripId}/groups/${groupId}`);
  },

  /**
   * Updates a user particiption status within a trip.
   * @param {Number} userId The user id
   * @param {Number} tripId The trip id.
   */
  updateGroupTripStatus(userId, tripId, payload) {
    return Repository.put(`/users/${userId}/trips/${tripId}/status`, payload);
  }
};
