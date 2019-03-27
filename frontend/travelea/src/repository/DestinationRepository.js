import Repository from "./Repository";


export default {
    getDestinations() {
        return Repository.get('/destinations');
    },

    getDestination(id) {
        return Repository.get(`/destinations/${id}`)
    },

    createDestination(payload) {
        return Repository.post('/destinations', payload);
    },

    updateDestination(id, payload) {
        return Repository.put(`/destinations/${id}`, payload)
    }    
}


