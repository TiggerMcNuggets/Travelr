import Vue from 'vue'
import Vuex from 'vuex'

import user from './user'
import users from './users/users'
import destReuqests from './EditRequests/requests'


Vue.use(Vuex);

export const store = new Vuex.Store({
    modules: {
        user: user,
        users: users,
        destinationEditRequests: destReuqests
    }
});



