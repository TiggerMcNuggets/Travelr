import Vue from "vue";
import Router from "vue-router";
import { store } from "../store/index";

// Components
import Trips from "../components/trips/Trips";
import Profile from "../components/profile/Profile";
import userSearch from "../components/userSearch/userSearch";
import Signup from "../components/signup/Signup.vue";
import Destination from "../components/destination/Destination";
import DestinationEdit from "../components/destination/DestinationEdit";
import Login from "../components/login/Login";
import Home from "../views/Home";
import ProfileDashboard from "../components/profile/ProfileDashboard";
import AdminDashboard from "../components/admin/AdminDashboard";
import EditProfile from "../components/profile/EditProfile.vue";
import Logout from "../components/logout/Logout.vue";
import ViewTrip from "../components/trips/ViewTrip.vue";
import SingleDestination from "../components/destination/SingleDestination";
import DestinationPage from "../components/destination/DestinationPage";
import Page from "../views/Page";
import Media from "../views/Media";
import UserGroupsPage from "../views/UserGroups";

const DEFAULT_ROUTE_AUTH = () => `/user/${store.getters.getUser.id}/dashboard`;
const DEFAULT_ROUTE_UNAUTH = () => "/login";

const authGuard = (to, from, next) => {
  if (!store.getters.getToken) return next(DEFAULT_ROUTE_UNAUTH());
  if (store.getters.getToken && !store.getters.getUser) {
    store
      .dispatch("fetchMe")
      .then(() => {
        // valid token, go next page
        return next();
      })
      .catch(() => {
        // invalid token, send to login
        return next(DEFAULT_ROUTE_UNAUTH());
      });
  } else {
    return next();
  }
};

const unauthGuard = (to, from, next) => {
  if (!store.getters.getToken) {
    return next();
  }
  if (store.getters.getToken && !store.getters.getUser) {
    store
      .dispatch("fetchMe")
      .then(() => {
        // valid token, send to home
        return next(DEFAULT_ROUTE_AUTH());
      })
      .catch(() => {
        // invalid token, go next page
        return next();
      });
  }
  return next(`/user/${store.getters.getUser.id}/dashboard`);
};

const standardAccessGuard = (to, from, next) => {
  if (!store.getters.getToken) return next(DEFAULT_ROUTE_UNAUTH());
  if (store.getters.getToken && !store.getters.getUser) {
    store
      .dispatch("fetchMe")
      .then(() => {
        // valid token
        if (
          to.params.id == store.getters.getUser.id ||
          store.getters.getIsUserAdmin
        ) {
          // User matches url parameter or is an admin, go next page
          return next();
        } else {
          // User is forbidden to access route, route back to current page
          return next(DEFAULT_ROUTE_AUTH());
        }
      })
      .catch(() => {
        // invalid token, send to login
        return next(DEFAULT_ROUTE_UNAUTH());
      });
  } else {
    if (
      to.params.id == store.getters.getUser.id ||
      store.getters.getIsUserAdmin
    ) {
      // User matches url parameter or is an admin, go next page
      return next();
    } else {
      // User is forbidden to access route, route back to current page
      return next(DEFAULT_ROUTE_AUTH());
    }
  }
  return next();
};

Vue.use(Router);
let router = new Router({
  mode: "history",
  routes: [
    {
      path: "",
      name: "home",
      component: Home,
      beforeEnter: unauthGuard,
      beforeLeave: unauthGuard,
      redirect: "/login",
      children: [
        {
          path: "/login",
          name: "login",
          component: Login,
          beforeEnter: unauthGuard,
          beforeLeave: unauthGuard
        },
        {
          path: "/signup",
          name: "signup",
          component: Signup,
          beforeEnter: unauthGuard,
          beforeLeave: unauthGuard
        }
      ]
    },

    {
      path: "/user/:id",
      component: Page,
      beforeEnter: authGuard,
      redirect: "/user/:id/dashboard",
      children: [
        {
          path: "dashboard",
          name: "travellerProfileDashboard",
          component: ProfileDashboard
        },
        {
          path: "profile",
          name: "Profile",
          component: Profile,
          beforeEnter: authGuard
        },
        {
          path: "edit",
          name: "editProfile",
          component: EditProfile,
          beforeEnter: standardAccessGuard
        },
        {
          path: "/destinations",
          name: "destinationPage",
          component: DestinationPage,
          beforeEnter: authGuard
        },
        {
          path: "/usergroups",
          name: "userGroupPage",
          component: UserGroupsPage,
          beforeEnter: authGuard
        },
        {
          path: "trips",
          name: "travellerTrips",
          component: Trips
        },
        {
          path: "/user/:id/trips/:trip_id",
          name: "view-trip",
          component: ViewTrip
        },
        {
          path: "media",
          name: "travellerProfilePhotos",
          component: Media
        },
        {
          path: "destinations",
          name: "travellerDestination",
          component: Destination,
          beforeEnter: standardAccessGuard
        },
        {
          path: "destinations/:dest_id",
          name: "single-destination",
          component: SingleDestination
        },
        {
          path: "destinations/edit/:dest_id",
          name: "edit-destination",
          component: DestinationEdit,
          beforeEnter: standardAccessGuard
        },
        {
          path: "/admin_dash",
          name: "admin_dash",
          component: AdminDashboard,
          beforeEnter: authGuard,
          meta: {
            requiresAdmin: true
          }
        },
        {
          path: "/users",
          name: "userSearch",
          component: userSearch,
          beforeEnter: authGuard
        }
      ]
    },

    {
      path: "/logout",
      name: "logout",
      component: Logout
    }
  ]
});

export default router;
