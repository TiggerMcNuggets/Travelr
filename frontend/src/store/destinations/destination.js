
export default {
    state: {
        destinations: []
    },

    mutations: {
        setDestinations(state, destinations) {
            state.destinations = destinations
        }
    },

    actions: {
        
    },

    getters: {
        getDestinations: state => state.destinations,
    },
}