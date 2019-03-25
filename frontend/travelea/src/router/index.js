import Vue from "vue";
import Router from "vue-router";

// Components
import Profile from "../components/profile/Profile"
import Signup from "../components/signup/Signup.vue"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import Login from "../components/login/Login"
import PersonalPhotos from "../components/profile/PersonalPhotos"
import CreateTrips from "../components/trips/CreateTrips.vue";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/home',
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
      path: '/trips/create',
      name: 'create-trip',
      component: CreateTrips
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
    },
    {
      path: "/signup",
      name: 'signup',
      component: Signup
    }
  ]
})
