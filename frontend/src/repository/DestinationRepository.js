import Repository from "./Repository";

export default {
  // Gets all the destinations which belong to a user.
  getDestinations(id) {
    return Repository.get(`/users/${id}/destinations`);
  },

  // Gets a single destination for a user.
  getDestination(id, destId) {
    return Repository.get(`/users/${id}/destinations/${destId}`);
  },

  // Creates a destination for a user given some valid data
  createDestination(userId, payload) {
    return Repository.post(`/users/${userId}/destinations`, payload);
  },

  // Updates a users destination valid destination data.
  updateDestination(userId, destId, payload) {
    return Repository.put(`/users/${userId}/destinations/${destId}`, payload);
  },

  // Makes a destination public
  makePublic(destId) {
    return Repository.put(`/destinations/${destId}/make_public`);
  },

  // Makes a destination private
  makePrivate(destId) {
    return Repository.put(`/destinations/${destId}/make_private`);
  },

  // Deletes a users destination
  deleteDestination(userId, destinationId) {
    return Repository.put(`/users/${userId}/destinations/${destinationId}/toggle_deleted`);
  },

  // Gets all edit requests for destinations for the admin user.
  getEditRequests() {
    return Repository.get('/destinations/edit_requests');
  },

  // Denys a specified edit request 
  denyEditRequest(requestId) {
    return Repository.post(`/destinations/edit_requests/${requestId}/deny`);
  },

  // Accepts a specified edit request
  acceptEditRequest(requestId) {
    return Repository.post(`/destinations/edit_requests/${requestId}/accept`);
  },

  // Adds a destination edit request.
  addDestinationEditRequest(payload) {
    return Repository.post(`/destinations/edit_requests`, payload)
  }
}
