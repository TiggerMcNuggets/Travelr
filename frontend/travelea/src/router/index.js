import Vue from 'vue'
import Router from 'vue-router'

// Components
import Profile from "../components/profile/Profile"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import Login from "../components/common/Login"
import PersonalPhotos from "../components/profile/PersonalPhotos"


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/profile',
      name: 'profile',
      component: Profile
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

    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/personalphotos/:id',
      name: 'personal-photos',
      component: PersonalPhotos
    }
  ]
})
