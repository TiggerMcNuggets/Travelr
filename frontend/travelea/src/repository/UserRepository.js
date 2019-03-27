import Repository from "./Repository";


export default {

    getUsers(params) {
        return Repository.get('/travellers_filter', {
            params: params
        });
    },

    getUser(id) {
        return Repository.get(`/travellers/${id}`);
    },

    createUser(payload) {
        return Repository.post('/travellers', payload);
    },

    updateUser(id, payload) {
        return Repository.put(`/travellers/${id}`, payload);
    },

    getTravellerTypes() {
        return Repository.get(`/traveller-types`);
    },

    getMe() {
        return Repository.get('/travellers/me');
      },

    getNationalities() {
        return Repository.get(`/nationalities`);
    },

    deleteUser(userId) {
        return Repository.delete(`/travellers/${userId}`);
    }
}