import Repository from "./Repository";

export default {
    createTrip(tripBody) {
        return Repository.post('/trips', tripBody);
    },

    getUserTrips(user_id) {
        return Repository.get('/users/'+user_id+'/trips')
    },

    getTrip(userId, tripId) {
        return Repository.get(`/users/${userId}/trips/${tripId}`);
    },

    updateTrip(id, tripBody) {
        return Repository.put(`/trips/${id}`, tripBody);
    }
}
