import Vue from "vue";
import Router from "vue-router";
import {store} from "../store/index";

// Components
import Profile from "../components/profile/Profile"
import userSearch from "../components/userSearch/userSearch"
import Signup from "../components/signup/Signup.vue"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import Login from "../components/login/Login"
import PersonalPhotos from "../components/profile/PersonalPhotos"
import CreateTrips from "../components/trips/CreateTrips.vue";
import EditProfile from "../components/profile/EditProfile.vue";
import Logout from "../components/logout/Logout.vue"

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
      path: '/login',
      name: 'login',
      component: Login
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
      path: '/signup',
      name: 'signup',
      component: Signup
    },
    {
        path: '/users',
        name: 'userSearch',
        component: userSearch
    },
    {
      path: '/profile/edit',
      name: 'editProfile',
      component: EditProfile,
      meta: { 
        requiresAuth: true
      }
    },
    {
      path: '/logout',
      name: 'logout',
      component: Logout,
      meta: {
        requiresAuth: true
      }
    }
  ]
})

function myFunction() {

}

router.beforeEach((to, from, next) => {

  let user = store.getters.getUser;
      
  if(to.matched.some(record => record.meta.requiresAuth)) {
    if (store.getters.isLoggedIn) {
      next();
      return;
    }
    const tokenFromCookies = localStorage.getItem("token");
    console.log(tokenFromCookies);
    if (tokenFromCookies === "") {
      next('/login');
      return;
    }
    // maybe getTime returns non error even when token null, yoiu have to figure out how to handle res
    store.dispatch("fetchMe")
    .then(() => {
      next();
      })
    .catch(e => console.log(e));
  } else {
    next() 
  }
});

export default router;