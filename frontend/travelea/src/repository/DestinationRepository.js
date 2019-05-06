import Repository from "./Repository";


export default {
    getDestinations(id) {
        return Repository.get(`/users/${id}/destinations`);
    },

    getDestination(user_id, dest_id) {
        return Repository.get(`/users/${user_id}/destinations/${dest_id}`)
    },

    createDestination(userId, payload) {
        return Repository.post(`/users/${userId}/destinations`, payload);
    },

    updateDestination(id, payload) {
        return Repository.put(`/destinations/${id}`, payload)
    }    
}


