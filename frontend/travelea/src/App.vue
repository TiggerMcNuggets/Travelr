<template>
  <v-app light>
    <v-navigation-drawer app temporary clipped v-model="drawer">
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
    <v-toolbar class="nav-bar" flat color="white" dense clipped-left fixed app>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <router-link to="/" ><v-toolbar-title>Travelr</v-toolbar-title></router-link>
      <v-spacer> </v-spacer>
      <v-btn pr-4>Sign Up</v-btn>
      <v-btn pl-4>Login </v-btn>
    </v-toolbar>
    <v-content class="main-container">
      <v-container fluid pa-0 ma-0 >
        <router-view></router-view>
      </v-container>
    </v-content>
  </v-app>
</template>

<style>

.main-container {
  margin-left: 0px;
}

.nav-bar {
  border-bottom: 1px solid #edeff1 !important;
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
        { name: "Sign Up", icon: "lock_open", link: "/signup" },
        { name: "Log In", icon: "lock_open", link: "/login" }
      ];

      if (store.getters.isLoggedIn) {
        menuOptions = [
          { name: "Profile", icon: "lock_open", link: "/user/"+store.getters.getUser.id },
          { name: "Users", icon: "lock_open", link: "/users" },
          { name: "My Destinations", icon: "lock_open", link: "/user/"+store.getters.getUser.id+"/destinations" },
          { name: "My Trips", icon: "lock_open", link: "/user/"+store.getters.getUser.id+"/trips" },         
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
