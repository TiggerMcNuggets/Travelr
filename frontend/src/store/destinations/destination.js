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
        async getDestinations({ commit }, userId) {
            const destinations = await DestinationRepository.getDestinations(userId);
            commit("setDestinations", destinations)
        },

        async postDestination({ dispatch }, userId, destination) {
            await DestinationRepository.createDestination(userId, destination);
            await dispatch("getDestinations", userId);
        },

        async putDestination({ dispatch }, userId, destinationId, destination) {
            await DestinationRepository.updateDestination(userId, destinationId, destination);
            await dispatch("getDestinations", userId);
        },

        async deleteDestination({ dispatch }, userId, destinationId) {
            await DestinationRepository.deleteDestination(userId, destinationId);
            await dispatch("getDestinations", userId);
        },
    },

    getters: {
        getDestinations: state => state.destinations,
    },
}