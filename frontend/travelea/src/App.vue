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
export default {
  name: "App",
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

      if (this.userLoggedIn) {
        menuOptions = [
          { name: "Dashboard", icon: "lock_open", link: "/home" },
          { name: "Users", icon: "lock_open", link: "/users" },
          { name: "My Trips", icon: "lock_open", link: "/trips/create" },
          { name: "My Destinations", icon: "lock_open", link: "/destination" }
        ];
      }

      return menuOptions;
    },
    userLoggedIn() {
      return true;
    }
  }
};
</script>
