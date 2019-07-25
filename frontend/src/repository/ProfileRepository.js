
import Repository from "./Repository";

export default {

  // Gets a users profile with the provided id.
  getProfile(id) {
    return Repository.get(`/travellers/${id}`);
  },

  // Edits a users profile with valid data.
  editProfile(payload, id) {
    return Repository.put(`/travellers/${id}`, payload);
  },
 }
