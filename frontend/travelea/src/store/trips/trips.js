import  { RepositoryFactory } from "../../repository/RepositoryFactory"
let tripRepository = RepositoryFactory.get("trip");
/**
 * Trips store sub-module
 */
export default {
    state: {
        trips: []
    },
    mutations: {
        async setTrips(state) {
            const trips = await tripRepository.getTrips();
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