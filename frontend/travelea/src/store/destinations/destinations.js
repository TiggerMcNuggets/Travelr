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

    },
    getters: {
        destinations(state) {
            return state.destinations;
        }
    }
}