import Vue from 'vue'
import Router from 'vue-router'

// Components
import Profile from "../components/profile/Profile"
import userSearch from "../components/userSearch/userSearch"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"


Vue.use(Router)

export default new Router({
    routes: [{
            path: '/profile',
            name: 'profile',
            component: Profile
        },
        {
            path: '/users',
            name: 'userSearch',
            component: userSearch
        },

        {
            path: '/destination',
            name: 'destination',
            component: Destination
        },

        {
            path: '/destination/edit/:id',
            name: 'edit-destination',
            component: DestinationEdit
        },
    ]
})