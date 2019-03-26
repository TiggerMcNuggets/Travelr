<template>
  <v-app>
    <v-navigation-drawer fixed clipped app v-model="drawer">
      <v-list>
        <v-list-tile v-for="item in menuOptions" :key="item.name" :to="item.link">
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>{{ item.name }}</v-list-tile-content>
        </v-list-tile>
        <v-list-tile v-if="loggedIn">
          <v-list-tile-action>
            <v-icon>lock_open</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>Logout</v-list-tile-content>
        </v-list-tile>

      </v-list>
    </v-navigation-drawer>
    <v-toolbar color="indigo" dark dense clipped-left fixed app>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-toolbar-title>TravelEA</v-toolbar-title>
    </v-toolbar>
    <v-content>
      <v-container fluid pa-0 ma-0>
        <router-view></router-view>
      </v-container>
    </v-content>
  </v-app>
</template>

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
        { name: "Sign Up", icon: "lock_open", link: "/signup" },
        { name: "Log In", icon: "lock_open", link: "/login" }
      ];

      if (store.getters.getUser) {
        menuOptions = [
          { name: "Dashboard", icon: "lock_open", link: "/profile" },
          { name: "Users", icon: "lock_open", link: "/users" },
          { name: "My Trips", icon: "lock_open", link: "/trips/create" },
          { name: "My Destinations", icon: "lock_open", link: "/destination" },
          { name: "Log Out", icon: "lock_open", link: "/logout"}
        ];
      }
      if (store.getters.getIsUserAdmin && store.getters.isLoggedIn) {
        menuOptions.push({ name: "Admin Panel", icon: "lock_open", link: "/admin_dash" });
      }

      return menuOptions;
    },

    loggedIn() {
      return store.getters.isLoggedIn
    }
  }
};
</script>
