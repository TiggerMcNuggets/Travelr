import { getTrips } from "../../repository/TripRepository";

/**
 * Trips store sub-module
 */
export default {
    state: {
        trips: []
    },
    mutations: {
        async setTrips(state, id) {
            const trips = await getTrips(id);
            state.trips = trips;
        },
    },
    actions: {

    },
    getters: {
        trips(state) {
            return state.trips;
        }
    }
}