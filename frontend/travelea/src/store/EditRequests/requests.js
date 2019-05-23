
/**
 * DestinationsRequest store sub-module
 */
export default {
    state: {
        DestEditRequests: []
    },
    mutations: {
        addRequest(state, destinationRequest) {
            state.DestEditRequests.push(destinationRequest);
        },

        setRequests(state, editRequests) {
            state.DestEditRequests = editRequests;
        }
    },
    actions: {
        setRequests({ commit }, editRequests) {
            commit("setRequests", editRequests);
        },
        addRequest({ commit }, destinationRequest) {
            commit("addRequest", destinationRequest);
        },
    },
    getters: {
        getRequests: state => state.DestEditRequests
    }
}
