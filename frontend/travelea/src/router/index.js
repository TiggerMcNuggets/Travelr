import Vue from "vue";
import Router from "vue-router";
import { store } from "../store/index";

// Components
import Home from "../components/home/Home.vue"
import Profile from "../components/profile/Profile"
import ProfilePhotos from "../components/profile/ProfilePhotos"
import ProfileTrips from "../components/profile/ProfileTrips"
import ProfileDestinations from "../components/profile/ProfileDestinations"
import userSearch from "../components/userSearch/userSearch"
import Signup from "../components/signup/Signup.vue"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import Login from "../components/login/Login"
import PersonalPhotos from "../components/profile/PersonalPhotos"
import ProfileDashboard from "../components/profile/ProfileDashboard"
import CreateTrips from "../components/trips/CreateTrips.vue";
import AdminDashboard from "../components/admin/AdminDashboard";
import EditProfile from "../components/profile/EditProfile.vue";
import Logout from "../components/logout/Logout.vue"
import ViewTrip from "../components/trips/ViewTrip.vue";

const authGuard = (to, from, next) => {
    if (!store.getters.getToken) return next("/login");
    if (store.getters.getToken && !store.getters.getUser) {
        store.dispatch("fetchMe")
        .then(() => {
            // valid token, go next page
            return next();
        })
        .catch(() => {
            // invalid token, send to login
            return next("/login");
        })
    } else {
        // TODO ADD META ADMIN CHECK HERE
        console.log(store.getters.getToken && store.getters.getUser);
        return next();
    }

};
const unauthGuard = (to, from, next) => {
    if (!store.getters.getToken) return next();
    if (store.getters.getToken && !store.getters.getUser) {
        store.dispatch("fetchMe")
        .then(() => {
            // valid token, send to home
            return next("/profile");
        })
        .catch(() => {
            // invalid token, go next page
            return next();
        })
    {
        path: '/trips/view/:id',
        name: 'view-trip',
        component: ViewTrip
    }
    return next("/profile");
}

Vue.use(Router);
let router = new Router({
    mode: 'history',
    routes: [
        {
            path:'',
            name: 'home',
            component: Home,
            beforeEnter: unauthGuard
        },
        {
            path: '/profile',
            name: 'profile',
            component: Profile,
            beforeEnter: authGuard,
            children: [
                {
                    path: '/profile',
                    name: 'profileDashboard',
                    component: ProfileDashboard                    
                },
                {
                    path: '/profile/photos',
                    name: 'profilePhotos',
                    component: ProfilePhotos                    
                },
        
                {
                    path: '/profile/trips',
                    name: 'profileTrips',
                    component: ProfileTrips                    
                },
                {
                    path: '/profile/edit',
                    name: 'editProfile',
                    component: EditProfile,
                },
                {
                    path: '/destinations',
                    name: 'destination',
                    component: Destination
                },
                {
                    path: '/destinations/edit/:id',
                    name: 'edit-destination',
                    component: DestinationEdit,
                    beforeEnter: authGuard
                },
            ]
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
            beforeEnter: unauthGuard
        },
        {
            path: '/personalphotos/:id',
            name: 'personal-photos',
            component: PersonalPhotos,
            beforeEnter: authGuard
        },
        {
            path: '/signup',
            name: 'signup',
            component: Signup,
            beforeEnter: unauthGuard
        },
        {
            path: '/users',
            name: 'userSearch',
            component: userSearch,
            beforeEnter: authGuard

        },
        {
            path: '/logout',
            name: 'logout',
            component: Logout
        },
        {
            path: "/admin_dash",
            name: 'admin_dash',
            component: AdminDashboard,
            beforeEnter: authGuard,
            meta: {
                requiresAdmin: true
            }
        },
    ]
});

export default router;