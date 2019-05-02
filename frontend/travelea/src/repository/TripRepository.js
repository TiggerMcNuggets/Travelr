import Repository from "./Repository";

export default {
    createTrip(tripBody) {
        return Repository.post('/trips', tripBody);
    },

    createTripForUser(tripBody, user_id) {
        return Repository.post('/users/'+user_id+'/trips', tripBody);
    },

    getTrips() {
        return Repository.get('/trips')
    },

    getUserTrips(user_id) {
        return Repository.get('/travellers/'+user_id+'/trips')
    },

    getTrip(id) {
        return Repository.get(`/trips/${id}`)
    },

    updateTrip(id, tripBody) {
        return Repository.put(`/trips/${id}`, tripBody)
    }
}
