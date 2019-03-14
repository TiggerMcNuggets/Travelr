import Vue from 'vue'
import Router from 'vue-router'

// Components
import Profile from "../components/profile/Profile"
import profileSearch from "../components/profileSearch/profileSearch"


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/profile',
      name: 'profile',
      component: Profile
    },
    {
      path: '/profile/search',
      name: 'profileSearch',
      component: profileSearch
    }
  ]
})
