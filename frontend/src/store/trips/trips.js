import TripRepository from "../../repository/TripRepository.js";

export default {
    state: {
        trips: [],
        selectedTrip: undefined
    },

    mutations: {
        setTrips(state, trips) {
            state.trips = trips
        },
        setTrip(state, trip) {
            state.selectedTrip = trip
        }
    },

    actions: {

        /**
         * Sends a request to get trips and updates store with this list
         * @param {Function} commit Allows to commit mutations
         * @param {Object} payload
         * @param {number} payload.userId The id of the user
         */
        async getTrips({ commit }, payload) {
            const trips = await TripRepository.getUserTrips(payload.userId);
            commit("setTrips", trips.data);
        },

        /**
         * Sends a request to get a selected trip and updates store with this trip
         * @param {Function} commit Allows to commit mutations
         * @param {Object} payload
         * @param {number} payload.userId The id of the user
         */
        async getTrip({ commit }, payload) {
            const trip = await TripRepository.getTrip(payload.userId, payload.tripId);
            commit("setTrip", trip.data);
        },

        /**
         * @param {Function} dispatch
         * @param {Object} payload
         * @param {Object} payload.trip
         * @param {number} payload.userId
         * @returns {Promise<void>}
         */
        async postTrip({ dispatch }, payload) {
            const res = await TripRepository.createTripForUser(payload.trip, payload.userId);
            await dispatch("getTrips", {userId: payload.userId});
            return res;
        },

        /**
         * @param {Function} dispatch
         * @param {Object} payload
         * @param {number} payload.userId
         * @param {number} payload.tripId
         * @param {Object} payload.trip
         * @returns {Promise<void>}
         */
        async putTrip({ dispatch }, payload) {
            await TripRepository.updateTrip(payload.userId, payload.tripId, payload.trip);
            await dispatch("getTrips", {userId: payload.userId});
        },

        /**
         * @param {Function} dispatch
         * @param {Object} payload
         * @param {number} payload.userId
         * @param {number} payload.tripId
         * @returns {Promise<void>}
         */
        async deleteTrip({ dispatch }, payload) {
            await TripRepository.deleteTrip(payload.userId, payload.tripId);
            await dispatch("getTrips", {userId: payload.userId});
        },
    },

    getters: {
        /**
         * @param {Object} state
         * @returns {Object}
         */
        getTrips: state => {
            return state.trips;
        },

        /**
         * @param {Object} state
         * @returns {Object}
         */
        getSelectedTrip: state => {
            return state.selectedTrip;
        },
    },
}