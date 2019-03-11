import Vue from 'vue'
import Router from 'vue-router'
import UserList from './components/User/UserList'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/users',
      name: 'users',
      component: UserList
    }
  ]
})
