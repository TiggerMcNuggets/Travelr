<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import { store } from "./store/index";

export default {
  name: "App",
  store,
  data() {
    return {
      drawer: true,
      mini: true,
      right: null
    };
  },
  computed: {
    menuOptions() {
      let menuOptions = [
        { name: "Sign Up", icon: "assignment_ind", link: "/signup" },
        { name: "Log In", icon: "lock_open", link: "/login" }
      ];

      if (store.getters.isLoggedIn) {
        menuOptions = [
          {
            name: "Profile",
            icon: "account_circle",
            link: "/user/" + store.getters.getUser.id
          },
          { name: "Users", icon: "supervised_user_circle", link: "/users" },
          { name: "Destination Map", icon: "map", link: "/destinations" },
          {
            name: "Destination List",
            icon: "list",
            link: "/user/" + store.getters.getUser.id + "/destinations"
          },
          {
            name: "My Trips",
            icon: "flight",
            link: "/user/" + store.getters.getUser.id + "/trips"
          }
        ];
      }
      if (store.getters.getIsUserAdmin && store.getters.isLoggedIn) {
        menuOptions.push({
          name: "Admin Panel",
          icon: "playlist_add_check",
          link: "/admin_dash"
        });
        menuOptions.push({
          name: "Edit Requests",
          icon: "done_all",
          link: "/edit_requests"
        });
      }

      return menuOptions;
    },

    loggedIn() {
      return store.getters.isLoggedIn;
    }
  },
  methods: {
    logout() {
      store
        .dispatch("logout")
        .then(() => {
          this.$router.push("/login");
        })
        .catch(() => {
          this.$router.push("/login");
        });
    }
  }
};
</script>
