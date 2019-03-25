import Repository from "./Repository";

export default {
  login(payload) {
    return Repository.post("/login", payload);
  },
  signup(payload) {
    return Repository.post("/travellers", payload);
  },
  logout() {
      return Repository.post('/logout');
  }
};
