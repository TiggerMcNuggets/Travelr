import Repository from "./Repository";

export default {
    createTrip(tripBody) {
        return Repository.post('/trips', tripBody);
    },

    createTripForUser(tripBody, user_id) {
        return Repository.post('/users/'+user_id+'/trips', tripBody);
    },

    getUserSingleTrip(user_id, trip_id) {
        return Repository.get('users/'+user_id+'/trips/'+trip_id)
    },

    getTrips() {
        return Repository.get('/trips');
    },

    getUserTrips(user_id) {
        return Repository.get('/travellers/'+user_id+'/trips');
    },

    getTrip(userId, tripId) {
        return Repository.get(`/users/${userId}/trips/${tripId}`);
    },

    updateTrip(id, tripBody) {
        return Repository.put(`/trips/${id}`, tripBody);
    }
}
