import Repository from "./Repository";

export default {
    createTrip(tripBody) {
        return Repository.post('/trips', tripBody);
    },

    getUserTrips(user_id) {
        return Repository.get('/users/'+user_id+'/trips')
    },

    getTrip(id) {
        return Repository.get(`/trips/${id}`)
    },

    updateTrip(id, tripBody) {
        return Repository.put(`/trips/${id}`, tripBody)
    }
}
