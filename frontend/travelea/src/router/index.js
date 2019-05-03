import Vue from "vue";
import Router from "vue-router";
import { store } from "../store/index";

// Components
import Home from "../components/home/Home.vue"
import Profile from "../components/profile/Profile"
import ProfilePhotos from "../components/profile/ProfilePhotos"
import ProfileTrips from "../components/profile/ProfileTrips"
import userSearch from "../components/userSearch/userSearch"
import Signup from "../components/signup/Signup.vue"
import Destination from "../components/destination/Destination"
import DestinationEdit from "../components/destination/DestinationEdit"
import Login from "../components/login/Login"
import ProfileDashboard from "../components/profile/ProfileDashboard"
import AdminDashboard from "../components/admin/AdminDashboard";
import EditProfile from "../components/profile/EditProfile.vue";
import Logout from "../components/logout/Logout.vue"
import ViewTrip from "../components/trips/ViewTrip.vue";
import SingleDestination from "../components/destination/SingleDestination.vue";
import Map from "../components/map/Map";

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
            return next("/user/"+store.getters.getUser.id);
        })
        .catch(() => {
            // invalid token, go next page
            return next();
        })
    }
    return next("/user/"+store.getters.getUser.id);
};

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
            path:'/maps',
            name: 'map',
            component: Map,
            beforeEnter: unauthGuard
        },
        {
            path: '/user/:id',
            name: 'userProfile',
            component: Profile,
            beforeEnter: authGuard,
            children: [
                {
                    path: '/user/:id',
                    name: 'travellerProfileDashboard',
                    component: ProfileDashboard                      
                },
                {
                    path: '/user/:id/edit',
                    name: 'editProfile',
                    component: EditProfile,
                },
                {
                    path: '/user/:id/trips',
                    name: 'travellerTrips',
                    component: ProfileTrips                    
                },
                {
                    path: '/user/:id/photos',
                    name: 'travellerProfilePhotos',
                    component: ProfilePhotos                    
                },
                {
                    path: '/user/:id/destinations',
                    name: 'travellerDestinations',
                    component: Destination                    
                },
                {
                    path: '/user/:id/destinations/edit/:dest_id',
                    name: 'edit-destination',
                    component: DestinationEdit,
                },
                {
                    path: '/user/:id/destinations/:dest_id',
                    name: 'travellerDestination',
                    component: SingleDestination                    
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
        {
            path: '/user/:user_id/trips/:trip_id',
            name: 'view-trip',
            component: ViewTrip
        }
    ]
});

export default router;