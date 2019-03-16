import { getDestination } from "../../repository/DestinationEditRepository";

/**
 * Destinations store sub-module
 */
export default {
    state: {
        destinations: []
    },
    mutations: {
        async setDestinations() {
            const destinations = await getDestination();
            this.state.destinations = destinations;
        }
    },
    actions: {

    },
    getters: {
        destinations(state) {
            return state.destinations;
        }
    }
}