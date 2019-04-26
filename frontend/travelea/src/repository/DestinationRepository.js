import Repository from "./Repository";


export default {
    getDestinations(userId) {
        return Repository.get(`/users/${userId}/destinations`);
    },

    getDestination(id) {
        return Repository.get(`/destinations/${id}`)
    },

    createDestination(userId, payload) {
        return Repository.post(`/users/${userId}/destinations`, payload);
    },

    updateDestination(id, payload) {
        return Repository.put(`/destinations/${id}`, payload)
    }    
}


