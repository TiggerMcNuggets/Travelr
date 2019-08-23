import TripRepository from "../../repository/TripRepository";

export default {
  state: {
    root: {        
    },
    selectedTrip: {
    },
    navigation: [
    ]
  },

  mutations: {
    setData(state, data) {
        state.root = data.root;
        state.selectedTrip = data.trip;
        state.navigation = data.navigation;
    },
  },

actions: {
    async getTrip({ commit }, payload) {
        const trip = await TripRepository.getTrip(payload.userId, payload.tripId);
        await commit("setData", trip.data);
    },

    async updateSelectedTrip({ commit }, payload) {

    }
},
  getters: {
    getSelectedTrip: state => state.selectedTrip,
    getRootTrip: state => state.root,
    getNavigation: state => state.navigation
  }
};
