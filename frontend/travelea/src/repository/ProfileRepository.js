
import Repository from "./Repository";

export default {
  getProfile(id) {
    return Repository.get(`/profile/${id}`);
  },
  editProfile(payload, id) {
    return Repository.put(`/profile/${id}`, payload);
  },
 }
