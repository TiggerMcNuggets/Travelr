import { getDestination } from "../../repository/DestinationEditRepository";

export default {
    state: {
        destinations: []
    },
    mutations: {
        async setDestinations() {
            const destinations = await getDestination();
            state.destinations = destinations;
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