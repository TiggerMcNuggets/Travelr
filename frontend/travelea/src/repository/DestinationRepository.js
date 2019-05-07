import Repository from "./Repository";


export default {
    getDestinations(id) {
        return Repository.get(`/users/${id}/destinations`);
    },

    getDestination(id, destId) {
        return Repository.get(`/users/${id}/destinations/${destId}`)
    },

    createDestination(userId, payload) {
        return Repository.post(`/users/${userId}/destinations`, payload);
    },

    updateDestination(id, payload) {
        return Repository.put(`/destinations/${id}`, payload)
    },

    makePublic(destId) {
        return Repository.post(`/destinations/${destId}/make_public`);
    }

    // deleteDestination(userId, destinationId) {
    //     return Repository.delete(`/users/${userId}/destinations/${destinationId}`);
    // }
}


