import DestinationRepository from "../../repository/DestinationRepository"

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
        async getDestinations({ commit }, payload) {
            const destinations = await DestinationRepository.getDestinations(payload.userId);
            commit("setDestinations", destinations)
        },

        async postDestination({ dispatch }, payload) {
            await DestinationRepository.createDestination(payload.userId, payload.destination);
            await dispatch("getDestinations", payload.userId);
        },

        async putDestination({ dispatch }, payload) {
            await DestinationRepository.updateDestination(payload.userId, payload.destinationId, payload.destination);
            await dispatch("getDestinations", payload.userId);
        },

        async deleteDestination({ dispatch }, payload) {
            await DestinationRepository.deleteDestination(payload.userId, payload.destinationId);
            await dispatch("getDestinations", payload.userId);
        },
    },

    getters: {
        getDestinations: state => state.destinations,
    },
}