import Repository from "./Repository";

export default {
    login(payload) {
        return Repository.post('/login', payload);
    },

    logout() {
        return Repository.post('/logout');
    },

    signup(payload) {
        return Repository.post('/travellers', payload);
    },

}
