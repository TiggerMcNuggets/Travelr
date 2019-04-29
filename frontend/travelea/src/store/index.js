import Vue from 'vue'
import Vuex from 'vuex'

import user from './user'
import users from './users/users'
import travellerTypes from './users/users'
import nationalities from './users/users'
import destinations from './destinations/destinations'

Vue.use(Vuex);

export const store = new Vuex.Store({
    modules: {
        user: user,
        destinations: destinations,
        users: users,
        travellerTypes: travellerTypes,
        nationalities: nationalities,
    }
});



