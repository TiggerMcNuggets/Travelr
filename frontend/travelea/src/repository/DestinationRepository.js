import Repository from "./Repository";


export default {
    getDestinations(id) {
        return Repository.get(`/users/${id}/destinations`);
    },

    getDestination(id) {
        return Repository.get(`/destinations/${id}`)
    },

    createDestination(userId, payload) {
        return Repository.post(`/users/${userId}/destinations`, payload);
    },

    updateDestination(id, payload) {
        return Repository.put(`/destinations/${id}`, payload)
    },

    // deleteDestination(userId, destinationId) {
    //     return Repository.delete(`/users/${userId}/destinations/${destinationId}`);
    // }
}


