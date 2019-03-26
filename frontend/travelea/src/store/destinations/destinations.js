import  { RepositoryFactory } from "../../repository/RepositoryFactory"
let DestinationRepository = RepositoryFactory.get("destination");


/**
 * Destinations store sub-module
 */
export default {
    state: {
        destinations: []
    },
    mutations: {
        setDestinations(state) {
            DestinationRepository.getDestinations()
            .then((res) => {
                state.destinations = res.data;
            })
            .catch((err) => {
                console.log(err)
            });
        },
    },
    actions: {
      async setDestinations(state) {
        state.destinations = await DestinationRepository.getDestinations().data;
      },
    },
    getters: {
        destinations(state) {
            return state.destinations;
        }
    }
}