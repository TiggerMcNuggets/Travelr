
import Repository from "./Repository";

export default {
  getProfile(id) {
    return Repository.get(`/travellers/${id}`);
  },
  getMe(id) {
    return Repository.get('/travellers/me');
  },
  editProfile(payload, id) {
    return Repository.put(`/travellers/${id}`, payload);
  },
 }
