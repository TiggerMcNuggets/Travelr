<template>
  <v-app>
    <v-navigation-drawer temporary clipped app v-model="drawer">
      <v-list>
        <v-list-tile v-for="item in menuOptions" :key="item.name" :to="item.link">
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>{{ item.name }}</v-list-tile-content>
        </v-list-tile>
        <v-list-tile v-if="loggedIn" v-on:click="logout">
          <v-list-tile-action>
            <v-icon>lock_open</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>Logout</v-list-tile-content>
        </v-list-tile>

      </v-list>
    </v-navigation-drawer>
    <v-toolbar color="indigo" dark dense clipped-left fixed app>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <router-link to="/" class="white--text"><v-toolbar-title>Travelr</v-toolbar-title></router-link>
    </v-toolbar>
    <v-content class="main-content">
      <v-container class="main-container" fluid pa-0 ma-0>
        <router-view></router-view>
      </v-container>
    </v-content>
  </v-app>
</template>

<style>
.main-content {
  padding-left: 0 !important;
  margin-left: 0 !important;
}

</style>

<script>

import {store} from "./store/index";

export default {
  name: "App",
  store,
  data() {
    return {
      drawer: false
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
          { name: "Profile", icon: "account_circle", link: "/user/"+store.getters.getUser.id },
          { name: "Users", icon: "supervised_user_circle", link: "/users" },
          { name: "Destination Map", icon: "map", link: "/destinations" },
          { name: "Destination List", icon: "list", link: "/user/"+store.getters.getUser.id+"/destinations" },
          { name: "My Trips", icon: "flight", link: "/user/"+store.getters.getUser.id+"/trips" },
        ];
      }
      if (store.getters.getIsUserAdmin && store.getters.isLoggedIn) {
        menuOptions.push({ name: "Admin Panel", icon: "playlist_add_check", link: "/admin_dash" });
        menuOptions.push({ name: "Edit Requests", icon: "done_all", link: "/edit_requests" });
      }

      return menuOptions;
    },

    loggedIn() {
      return store.getters.isLoggedIn
    }
  },
  methods: {
    logout() {
      store.dispatch("logout")
      .then(() => {
        
        this.$router.push("/login")
      })
      .catch(() => {
        this.$router.push("/login")
      })
    }
  }
};
</script>
