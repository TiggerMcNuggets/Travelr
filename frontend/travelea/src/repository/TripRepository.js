import Repository from "./Repository";

export default {
    createTrip(tripBody) {
        return Repository.post('/trips', tripBody);
    },

    getTrips() {
        return Repository.get('/trips')
    },

    getTrip(id) {
        return Repository.get(`/trips/${id}`)
    },

    updateTrip(id, tripBody) {
        return Repository.put(`/trips/${id}`, tripBody)
    }
}
