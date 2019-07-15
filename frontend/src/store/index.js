import Vue from 'vue'
import Vuex from 'vuex'

import user from './user'
import users from './users/users'
import destinations from './destinations/destinations';
import trips from './trips/trips.js'
Vue.use(Vuex);

export const store = new Vuex.Store({
    modules: {
        user: user,
        users: users,
        destinations: destinations,
        trips: trips
    }
});



