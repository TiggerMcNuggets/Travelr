import Vue from 'vue'
import Vuex from 'vuex'

import user from './user'
import destinations from './destinations/destinations'
import trips from './trips/trips'


Vue.use(Vuex)


export const store = new Vuex.Store({
  modules: {
    user: user,
    destinations: destinations,
    trips: trips,
  }
});
