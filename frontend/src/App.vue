<template>
  <div id="app">
    <v-app id="inspire">
      <v-toolbar fixed app clipped-right class="main-header">
        <router-link to="/" class="primary-logo">
          <v-toolbar-title class="fill-height">
            <img class="fill-height" src="./assets/logo_white.png" />
          </v-toolbar-title>
        </router-link>
      </v-toolbar>
      <v-navigation-drawer fixed v-model="drawer" app :mini-variant.sync="mini" hide-overlay>
        <v-toolbar flat class="transparent">
          <v-list class="pa-0">
            <v-list-tile avatar>
              <v-list-tile-avatar>
                <img src="https://randomuser.me/api/portraits/men/85.jpg" />
              </v-list-tile-avatar>

              <v-list-tile-content>
                <v-list-tile-title>John Leider</v-list-tile-title>
              </v-list-tile-content>

              <v-list-tile-action>
                <v-btn icon @click.stop="mini = !mini">
                  <v-icon>chevron_left</v-icon>
                </v-btn>
              </v-list-tile-action>
            </v-list-tile>
          </v-list>
        </v-toolbar>

        <v-list class="pt-0">
          <v-divider></v-divider>
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
      <v-content>
        <v-container fluid fill-height ma-0 pa-0>
          <v-layout>
            <v-flex>
              <router-view></router-view>
            </v-flex>
          </v-layout>
        </v-container>
      </v-content>
    </v-app>
  </div>
</template>



<style lang="scss">
@import "./assets/css/main.scss";

.primary-logo {
  height: 50%;
}

.fill-height {
  height: 100%;
}
</style>


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
