import Vue from 'vue'
import Router from 'vue-router'

// Components
import Profile from "../components/profile/Profile"
import userSearch from "../components/userSearch/userSearch"


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/profile',
      name: 'profile',
      component: Profile
    },
    {
      path: '/users',
      name: 'userSearch',
      component: userSearch
    }
  ]
})
