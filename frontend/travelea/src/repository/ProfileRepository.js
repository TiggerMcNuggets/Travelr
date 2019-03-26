
import Repository from "./Repository";

export default {
  getProfile(id) {
    return Repository.get(`/travellers/${id}`);
  },
  editProfile(payload, id) {
    return Repository.put(`/travellers/${id}`, payload);
  },
 }
