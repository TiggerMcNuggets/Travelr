export default {
  /**
   * Store the state of the global snackbar notifications
   */

  state: {     
    text: 'Default Text',
    color: 'green',
    time: undefined
  },

  mutations: {
    setSnack(state, snack) {
      state.text = snack.text;
      state.color = snack.color;
      state.time = snack.time;
    },
    removeSnacky(state) {
      state.text = '';
    }
  },
  actions: {
    resetSnackbar({commit}) {
      commit("removeSnacky");
    },
    setSnackbar({commit}, snack) {
      commit("setSnack", snack);
    }
  },
  getters: {
    getSnackbarColor: state => state.color,
    getSnackbarText: state => state.text,
    getSnackbarTime: state => state.time,
  }
}