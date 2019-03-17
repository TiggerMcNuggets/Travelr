import { getDestination } from "../../repository/DestinationEditRepository";

/**
 * Destinations store sub-module
 */
export default {
    state: {
        destinations: []
    },
    mutations: {
        async setDestinations(state) {
            const destinations = await getDestination();
            state.destinations = destinations;
        },
    },
    actions: {

    },
    getters: {
        destinations(state) {
            return state.destinations;
        }
    }
}