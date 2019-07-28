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
        /**
         * Sends a request to get destinations and updates store with this list
         * @param {Function} commit Allows to commit mutations
         * @param {number} userId The id of the user
         */
        async getDestinations({ commit }, payload) {
            const destinations = await DestinationRepository.getDestinations(payload.userId);
            commit("setDestinations", destinations.data)
        },

        /**
         * Sends a request to post a destination and updates store
         * @param dispatch Allows user to perform actions
         * @param payload The data that includes userId and destination
         * @returns {Promise<void>}
         */
        async postDestination({ dispatch }, payload) {
            const res = await DestinationRepository.createDestination(payload.userId, payload.destination);
            await dispatch("getDestinations", {userId: payload.userId});
            return res;
        },

        /**
         * Sends a request to update a destination and updates store
         * @param dispatch Allows user to perform actions
         * @param payload The data that includes userId, destinationID, and destination
         * @returns {Promise<void>}
         */
        async putDestination({ dispatch }, payload) {
            await DestinationRepository.updateDestination(payload.userId, payload.destinationId, payload.destination);
            await dispatch("getDestinations", {userId: payload.userId});
        },

        /**
         * Sends a request to delete a destination and updates store
         * @param dispatch Allows user to perform actions
         * @param payload The data that includes userId and destinationId
         * @returns {Promise<void>}
         */
        async deleteDestination({ dispatch }, payload) {
            await DestinationRepository.deleteDestination(payload.userId, payload.destinationId);
            await dispatch("getDestinations", {userId: payload.userId});
        },
    },

    getters: {
        /**
         * Returns the list of destinations in the store
         * @param state The store's current state
         * @returns {*} List of destinations
         */
        getDestinations: state => {
            return state.destinations;
        },
    },
}