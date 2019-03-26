import Vue from "vue";
import Router from "vue-router";
import { store } from "../store/index";

// Components
import Profile from "../components/profile/Profile"
import userSearch from "../components/userSearch/userSearch"
import Signup from "../components/signup/Signup.vue"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import Login from "../components/login/Login"
import PersonalPhotos from "../components/profile/PersonalPhotos"
import CreateTrips from "../components/trips/CreateTrips.vue";
import AdminDashboard from "../components/admin/AdminDashboard";
import EditProfile from "../components/profile/EditProfile.vue";
import Logout from "../components/logout/Logout.vue"

Vue.use(Router);
let router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/home',
            name: 'profile',
            component: Profile,
            beforeEnter: authGuard,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/destination',
            name: 'destination',
            component: Destination,
            beforeEnter: authGuard,
            meta: {
                requiresAuth: true
            }
        },

        {
            path: '/destination/edit/:id',
            name: 'edit-destination',
            component: DestinationEdit,
            beforeEnter: authGuard,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/trips/create',
            name: 'create-trip',
            component: CreateTrips,
            beforeEnter: authGuard,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
            beforeEnter: authGuard,
            meta: {
                requiresUnauth: true
            }
        },
        {
            path: '/personalphotos/:id',
            name: 'personal-photos',
            component: PersonalPhotos,
            beforeEnter: authGuard,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/signup',
            name: 'signup',
            component: Signup,
            beforeEnter: authGuard,
            meta: {
                requiresUnauth: true
            }
        },
        {
            path: '/users',
            name: 'userSearch',
            component: userSearch,
            beforeEnter: authGuard,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/profile/edit',
            name: 'editProfile',
            component: EditProfile,
            beforeEnter: authGuard,
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
        },
        {
            path: "/admin_dash",
            name: 'admin_dash',
            component: AdminDashboard,
            beforeEnter: authGuard,
            meta: {
                requiresAdmin: true,
                requiresAuth: true
            }
        }
    ]
});


const authGuard = (to, from, next) => {
    if (!store.getters.getToken) return next("/login");
    if (store.getters.getToken && !store.getters.getUser) {
        return store.dispatch("fetchMe")
        .then(() => {
            // valid token, go next page
            next();
        })
        .catch(() => {
            // invalid token, send to login
            next("/login");
        })
    };

    return next();
};
let unauthGuard = (to, from, next) => {
    if (!store.getters.getToken) return next();
    if (store.getters.getToken && !store.getters.getUser) {
        return store.dispatch("fetchMe")
        .then(() => {
            // valid token, go next page
            next("/home");
        })
        .catch(() => {
            // invalid token, send to login
            next();
        })
    };

    next("/home");
}

export default router;