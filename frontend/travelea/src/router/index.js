import Vue from "vue";
import Router from "vue-router";
import {store} from "../store/index";

// Components
import Profile from "../components/profile/Profile"
import Signup from "../components/signup/Signup.vue"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import PersonalPhotos from "../components/profile/PersonalPhotos"
import CreateTrips from "../components/trips/CreateTrips.vue";
import EditProfile from "../components/profile/EditProfile.vue";

Vue.use(Router)

let router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/home',
      name: 'profile',
      component: Profile,
      meta: { 
        requiresAuth: true
      }
    },
    {
      path: '/destination',
      name: 'destination',
      component: Destination,
      meta: { 
        requiresAuth: true
      }
    },

    {
      path: '/destination/edit/:id',
      name: 'edit-destination',
      component: DestinationEdit,
      meta: { 
        requiresAuth: true
      }
    },
    {
      path: '/trips/create',
      name: 'create-trip',
      component: CreateTrips,
      meta: { 
        requiresAuth: true
      }
    },
    {
      path: '/personalphotos/:id',
      name: 'personal-photos',
      component: PersonalPhotos,
      meta: { 
        requiresAuth: true
      }
    },
    {
      path: "/signup",
      name: 'signup',
      component: Signup
    },
    {
      path: '/profile/edit',
      name: 'editProfile',
      component: EditProfile,
      meta: { 
        requiresAuth: true
      }
    }

  ]
})

router.beforeEach((to, from, next) => {
  if(to.matched.some(record => record.meta.requiresAuth)) {
    console.log(store.getters.isLoggedIn);
    console.log(store.getters.getUser);  
    if (store.getters.isLoggedIn) {
      next()
      return
    }
    next('/login') 
  } else {
    next() 
  }
})

export default router;